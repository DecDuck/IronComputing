package com.decduck3.ironcomputing.forge.blockentity;

import com.decduck3.ironcomputing.block.IronComputingBlocks;
import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.blockentity.IronComputingBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class IronComputingBlockEntitiesForge extends IronComputingBlockEntities {

    public static void init() {
        COMPUTER_BLOCK_ENTITY_TYPE = registerBlockEntity("computer", () -> BlockEntityType.Builder.of(Computer::new, IronComputingBlocks.COMPUTER_CASE_BLOCK.get()).build(null));

        initRegistry();
    }
}
