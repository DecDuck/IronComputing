package com.decduck3.ironcomputing.gui.firmware;

import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.blockentity.FirmwareStation;
import com.decduck3.ironcomputing.gui.IronComputingMenuTypes;
import com.decduck3.ironcomputing.gui.slot.ROMSlot;
import com.decduck3.ironcomputing.state.CCInvLayout;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FirmwareStationMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    // Clientside constructor
    public FirmwareStationMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(Computer.INVENTORY_SIZE), new SimpleContainerData(0));
    }

    // Server-side constructor
    public FirmwareStationMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(IronComputingMenuTypes.FIRMWARE_STATION.get(), containerId);
        checkContainerSize(container, FirmwareStation.CONTAINER_SIZE);
        checkContainerDataCount(containerData, 0);

        this.container = container;
        this.containerData = containerData;

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // Player Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addSlot(new ROMSlot(container, 0, 62, 35));

        this.addDataSlots(containerData);
    }

    // TODO: write quickMoveStack handler that respects slot stack size
    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public ItemStack getRom() {
        return container.getItem(0);
    }
}
