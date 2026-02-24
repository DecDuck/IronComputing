package com.decduck3.ironcomputing.gui.computer;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.gui.IronComputingMenuTypes;
import com.decduck3.ironcomputing.state.CCInvLayout;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ComputerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    // Clientside constructor
    public ComputerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(Computer.INVENTORY_SIZE), new SimpleContainerData(1));
    }

    // Server-side constructor
    public ComputerMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(IronComputingMenuTypes.COMPUTER.get(), containerId);
        checkContainerSize(container, Computer.INVENTORY_SIZE);
        checkContainerDataCount(containerData, 1);

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

        // Add computer stuff
        for (int i = 0; i < Computer.INVENTORY_LAYOUT.length; i++) {
            CCInvLayout layout = Computer.INVENTORY_LAYOUT[i];

            this.addSlot(layout.createSlot(container, i));
        }

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
}
