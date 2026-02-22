package com.decduck3.ironcomputing.gui.slot;

import com.decduck3.ironcomputing.item.CPUItem;
import com.decduck3.ironcomputing.item.RAMItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RAMSlot extends Slot {
    public RAMSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof RAMItem;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
