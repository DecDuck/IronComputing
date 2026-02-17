package com.decduck3.ironcomputing.ironserver;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.ironserver.packets.PacketInterface;
import com.decduck3.ironcomputing.ironserver.packets.PingPacketInterface;
import com.decduck3.ironcomputing.proto.Main;
import com.google.protobuf.ByteString;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class IronServer {
    public static IronServer INSTANCE;
    public static final IronServerPacketSync<String, Main.RustJava> PACKET_SYNCER = new IronServerPacketSync<>();

    public static final PingPacketInterface PING_PACKET_INTERFACE = new PingPacketInterface();
    private static final PacketInterface<?, ?>[] packetInterfaces = new PacketInterface[]{PING_PACKET_INTERFACE};

    private final Process SERVER_PROCESS;
    private final Thread SERVER_RECIEVE_THREAD;
    private Socket SERVER_SOCKET;
    private boolean shutdown = false;

    public static IronServer createIronServer() {
        // Platform side check
        try {
            return new IronServer();
        } catch (WrongSideException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private IronServer() throws WrongSideException, IOException {
        String[] executable = getExecutable();
        SERVER_PROCESS = new ProcessBuilder(executable).start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        IronServerLogger errorLogger = new IronServerLogger(SERVER_PROCESS.getErrorStream(), true);
        IronServerLogger stdoutLogger = new IronServerLogger(SERVER_PROCESS.getInputStream(), false);
        errorLogger.start();
        stdoutLogger.start();

        while (!this.shutdown) {
            try {
                tryConnectSocket();
                break;
            } catch (SocketException e) {
                IronComputing.LOGGER.info("socket not listening, waiting for server start...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        SERVER_RECIEVE_THREAD = new Thread(() -> {
            try {
                InputStream socketRead = SERVER_SOCKET.getInputStream();
                while (true) {
                    ByteBuffer lengthBuffer = ByteBuffer.allocate(8);
                    lengthBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    socketRead.readNBytes(lengthBuffer.array(), 0, 8);

                    long size = lengthBuffer.getLong();
                    byte[] packetBuffer = socketRead.readNBytes((int) size);
                    Main.RustJava packet = Main.RustJava.parseFrom(packetBuffer);

                    for (PacketInterface<?, ?> packetInterface : packetInterfaces) {
                        if (packet.getMessageType() == packetInterface.messageType()) {
                            packetInterface.receiveRaw(packet.getData());
                        }
                    }

                    PACKET_SYNCER.put(packet.getId(), packet, 5, TimeUnit.MINUTES);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SERVER_RECIEVE_THREAD.start();
    }

    private void tryConnectSocket() throws IOException {
        if (this.SERVER_SOCKET != null && this.SERVER_SOCKET.isConnected()) return;
        this.SERVER_SOCKET = new Socket("127.0.0.1", 10450);
    }

    private String[] getExecutable() {
        if (Platform.isDevelopmentEnvironment()) {
            return new String[]{"cargo", "run", "--manifest-path", "../../ironserver/Cargo.toml"};
        } else {
            String operatingSystem = System.getProperty("os.name");
            String osCode = operatingSystem.substring(0, 3).toLowerCase();

            try (var resourceStream = IronComputing.class.getClassLoader().getResourceAsStream(String.format("ironserver-%s.bin", osCode))) {
                if (resourceStream == null) {
                    throw new IOException("No ironserver binary");
                }
                String extension = operatingSystem.toLowerCase().startsWith("win") ? ".exe" : "";
                String filename = "ironserver" + extension;

                String tmpPath = System.getProperty("java.io.tmpdir");
                Path extractPath = Paths.get(tmpPath, filename);

                Files.copy(resourceStream, extractPath);
                return new String[]{extractPath.toString()};
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writePacket(ByteString packet) throws IOException {
        if(!IronComputing.isServer()) return;
        tryConnectSocket();
        ByteBuffer buffer = ByteBuffer.allocate(8 + packet.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(packet.size());
        buffer.put(packet.toByteArray());
        this.SERVER_SOCKET.getOutputStream().write(buffer.array());
    }

    public void destroy() {
        this.shutdown = true;
        SERVER_PROCESS.destroyForcibly();

    }

}
