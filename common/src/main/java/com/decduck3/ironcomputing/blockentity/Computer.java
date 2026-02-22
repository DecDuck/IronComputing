package com.decduck3.ironcomputing.blockentity;

import com.decduck3.ironcomputing.gui.ComputerMenu;
import com.decduck3.ironcomputing.state.CCInvLayout;
import com.decduck3.ironcomputing.state.CCType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Computer extends BaseContainerBlockEntity {
    // Index is the index in the container
    // CCInvLayout stores type (for slot filter) and position on the screen texture
    public static final CCInvLayout[] INVENTORY_LAYOUT = new CCInvLayout[]{  // linebreak
            new CCInvLayout(CCType.CPU, 80, 37), // linebreak
            new CCInvLayout(CCType.RAM, 101, 27),  // linebreak
            new CCInvLayout(CCType.RAM, 101, 48),  // linebreak
            new CCInvLayout(CCType.RAM, 121, 27), // linebreak
            new CCInvLayout(CCType.RAM, 121, 48), // linebreak
            new CCInvLayout(CCType.ROM, 80, 16), // linebreak
            new CCInvLayout(CCType.Disk, 80, 58), // linebreak
            new CCInvLayout(CCType.Peripheral, 19, 28), // linebreak
            new CCInvLayout(CCType.Peripheral, 19, 49), // linebreak
            new CCInvLayout(CCType.Peripheral, 39, 28), // linebreak
            new CCInvLayout(CCType.Peripheral, 39, 49), // linebreak
            new CCInvLayout(CCType.Peripheral, 59, 28), // linebreak
            new CCInvLayout(CCType.Peripheral, 59, 49), // linebreak
    };

    private ContainerData data;

    public static final int INVENTORY_SIZE = INVENTORY_LAYOUT.length;
    public NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public Computer(BlockPos pos, BlockState blockState) {
        super(IronComputingBlockEntities.COMPUTER_BLOCK_ENTITY_TYPE.get(), pos, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return -1;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }


    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.ironcomputing.computer_case");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ComputerMenu(containerId, inventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return this.items.get(slot).split(amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return this.items.remove(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
