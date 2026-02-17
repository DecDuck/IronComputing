package com.decduck3.ironcomputing.ironserver.packets;

import com.decduck3.ironcomputing.proto.Main;
import com.google.protobuf.ByteString;

import java.nio.charset.Charset;

public class PingPacketInterface extends PacketInterface<String, String> {
    @Override
    ByteString serialize(String message) {
        return ByteString.copyFromUtf8(message);
    }

    @Override
    String deserialize(ByteString data) {
        return data.toString(Charset.defaultCharset());
    }

    @Override
    public Main.MessageType messageType() {
        return Main.MessageType.PING;
    }
}
