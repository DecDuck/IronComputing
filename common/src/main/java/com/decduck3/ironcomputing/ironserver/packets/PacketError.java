package com.decduck3.ironcomputing.ironserver.packets;

public class PacketError extends Exception {
    public PacketError(String message) {
        super(message);
    }
}
