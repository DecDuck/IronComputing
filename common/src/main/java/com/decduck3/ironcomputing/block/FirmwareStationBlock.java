package com.decduck3.ironcomputing.block;

import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.blockentity.FirmwareStation;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FirmwareStationBlock extends UIBlockEntityBlock<FirmwareStation> {
    public FirmwareStationBlock() {
        super(IronComputingBlocks.baseProperties(), FirmwareStation::new);
    }

}
