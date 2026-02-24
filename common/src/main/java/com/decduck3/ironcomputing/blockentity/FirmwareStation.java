package com.decduck3.ironcomputing.blockentity;

import com.decduck3.ironcomputing.gui.computer.ComputerMenu;
import com.decduck3.ironcomputing.gui.firmware.FirmwareStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FirmwareStation extends ContainerBlockEntity {
    public static final int CONTAINER_SIZE = 1;
    private final ContainerData data = new SimpleContainerData(0);


    public FirmwareStation(BlockPos pos, BlockState blockState) {
        super(IronComputingBlockEntities.FIRMWARE_STATION_ENTITY_TYPE.get(), pos, blockState, CONTAINER_SIZE);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.ironcomputing.firmware_station");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FirmwareStationMenu(containerId, inventory, this, this.data);
    }
}
