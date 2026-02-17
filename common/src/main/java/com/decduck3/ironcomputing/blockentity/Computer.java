package com.decduck3.ironcomputing.blockentity;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.ironserver.IronServer;
import com.decduck3.ironcomputing.ironserver.packets.PacketError;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;

public class Computer extends BlockEntity {
    public Computer(BlockPos pos, BlockState blockState) {
        super(IronComputingBlockEntities.COMPUTER_BLOCK_ENTITY_TYPE.get(), pos, blockState);

        try {
            String response = IronServer.PING_PACKET_INTERFACE.send("Hello World", true);
            if(response == null) return;
            System.out.println(response);
        } catch (PacketError | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
