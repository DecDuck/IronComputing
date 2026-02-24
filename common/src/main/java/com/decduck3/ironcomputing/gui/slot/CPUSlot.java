package com.decduck3.ironcomputing.gui.slot;

import com.decduck3.ironcomputing.item.CPUItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CPUSlot extends Slot {
    public CPUSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof CPUItem;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
