package com.decduck3.ironcomputing.network;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.gui.firmware.FirmwareStationMenu;
import com.decduck3.ironcomputing.ironserver.IronServer;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class ROMUpload {
    public static final ResourceLocation ROM_UPLOAD_PACKET_ID = ResourceLocation.tryBuild(IronComputing.MOD_ID, "rom_upload");
    private static final Map<UUID, List<byte[]>> UPLOAD_DATA_MAP = new HashMap<>();

    private final int index;
    private final byte[] content;
    private final int length;

    public ROMUpload(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readByteArray(), buf.readInt());
    }

    public ROMUpload(int index, byte[] content, int length) {
        this.index = index;
        this.content = content;
        this.length = length;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeByteArray(this.content);
        buf.writeInt(this.length);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier){
        if(this.content == null || this.length == 0) {
            IronComputing.LOGGER.warn("malformed rom upload packet");
            return;
        }

        // Hard cap on file size
        if(this.length > 160){
            return;
        }

        NetworkManager.PacketContext context = contextSupplier.get();
        AbstractContainerMenu containerMenu = context.getPlayer().containerMenu;
        if(!(containerMenu instanceof FirmwareStationMenu stationMenu)) {
            return;
        }
        ItemStack rom = stationMenu.getRom();
        if(rom.isEmpty()) {
            return;
        }

        UUID playerUuid = context.getPlayer().getUUID();
        if(this.index == 0) {
            List<byte[]> chunks = new ArrayList<>(Collections.nCopies(this.length, new byte[0]));
            chunks.set(this.index, this.content);
            UPLOAD_DATA_MAP.put(playerUuid, chunks);
        } else {
            List<byte[]> uploadMap = UPLOAD_DATA_MAP.get(playerUuid);;
            if(uploadMap == null) {
                IronComputing.LOGGER.warn("ROM upload start arrived out of order, going to get corrupted... (index {})", this.index);
                return;
            }
            if(this.index >= this.length) {
                IronComputing.LOGGER.warn("ROM upload index larger than specified");
            }
            uploadMap.set(this.index, this.content);
        }

        if(this.index == this.length - 1) {
            List<byte[]> uploadMap = UPLOAD_DATA_MAP.remove(playerUuid);
            if(uploadMap == null) {
                IronComputing.LOGGER.warn("ROM upload corrupted...");
                return;
            }
            boolean valid = uploadMap.stream().allMatch(e -> e.length > 0);
            if(!valid) {
                IronComputing.LOGGER.warn("ROM upload end arrived out of order, ending upload...");
                return;
            }

            int totalLength = uploadMap.stream().mapToInt(e -> e.length).sum();
            byte[] flat = new byte[totalLength];
            int offset = 0;
            for(byte[] data : uploadMap) {
                for(byte by : data) {
                    flat[offset] = by;
                    offset ++;
                }
            }

            IronServer.INSTANCE.uploadROM(flat, rom);
        }
    }

    public static void uploadContent(byte[] content) {
        double packetsFloat = ((double)content.length) / ((double) IronComputingNetwork.MAX_PACKET_PAYLOAD_SIZE);
        int packets = (int) Math.ceil(packetsFloat);
        IronComputing.LOGGER.info("uploading rom with length {}", packets);
        for(int i = 0; i < packets; i++) {
            int lower = i * IronComputingNetwork.MAX_PACKET_PAYLOAD_SIZE;
            int upper = (i + 1) * IronComputingNetwork.MAX_PACKET_PAYLOAD_SIZE;
            byte[] buffer = Arrays.copyOfRange(content, lower, upper);

            ROMUpload packet = new ROMUpload(i, buffer, packets);
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            packet.encode(buf);
            NetworkManager.sendToServer(ROM_UPLOAD_PACKET_ID, buf);
        }
    }
}
