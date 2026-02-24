package com.decduck3.ironcomputing.network;

import com.decduck3.ironcomputing.IronComputing;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;

public class IronComputingNetwork {
    public static final int MAX_PACKET_PAYLOAD_SIZE = 32000;

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ROMUpload.ROM_UPLOAD_PACKET_ID, ((buf, context) -> {
            ROMUpload upload = new ROMUpload(buf);
            upload.apply(() -> context);
        }));
    }
}
