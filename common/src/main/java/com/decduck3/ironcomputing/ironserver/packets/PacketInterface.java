package com.decduck3.ironcomputing.ironserver.packets;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.ironserver.IronServer;
import com.decduck3.ironcomputing.proto.Main;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class PacketInterface<Q, R> {
    abstract ByteString serialize(Q message);

    abstract R deserialize(ByteString data);

    public abstract Main.MessageType messageType();

    public void receive(R message) {
    }

    public void receiveRaw(ByteString data) {
        receive(deserialize(data));
    }

    public R send(Q message, boolean response) throws PacketError, IOException {
        if(IronServer.INSTANCE == null) return null;
        if(!IronComputing.isServer()) return null;

        ByteString serialized = serialize(message);
        Main.MessageType messageType = messageType();
        String messageId = UUID.randomUUID().toString();

        Main.JavaRust request = Main.JavaRust.newBuilder().setData(serialized).setMessageType(messageType).setId(messageId).build();
        ByteString rawPacket = request.toByteString();
        IronServer.INSTANCE.writePacket(rawPacket);

        if (!response) return null;

        Main.RustJava packetRaw = IronServer.PACKET_SYNCER.get(messageId, 5000, TimeUnit.DAYS);
        if (packetRaw.getMessageType() == Main.MessageType.ERROR) {
            throw new PacketError(packetRaw.getData().toString());
        }

        return deserialize(packetRaw.getData());
    }
}
