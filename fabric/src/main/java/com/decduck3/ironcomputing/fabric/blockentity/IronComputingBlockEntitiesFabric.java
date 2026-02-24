package com.decduck3.ironcomputing.fabric.blockentity;

import com.decduck3.ironcomputing.block.IronComputingBlocks;
import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.blockentity.FirmwareStation;
import com.decduck3.ironcomputing.blockentity.IronComputingBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

public class IronComputingBlockEntitiesFabric extends IronComputingBlockEntities {
    public static void init() {
        COMPUTER_BLOCK_ENTITY_TYPE = registerBlockEntity("computer", () -> FabricBlockEntityTypeBuilder.create(Computer::new, IronComputingBlocks.COMPUTER_CASE_BLOCK.get()).build());
        FIRMWARE_STATION_ENTITY_TYPE = registerBlockEntity("firmware_station", () -> FabricBlockEntityTypeBuilder.create(FirmwareStation::new, IronComputingBlocks.FIRMWARE_STATION_BLOCK.get()).build());

        initRegistry();
    }
}
