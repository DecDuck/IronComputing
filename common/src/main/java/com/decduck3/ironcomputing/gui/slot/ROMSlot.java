package com.decduck3.ironcomputing.gui.slot;

import com.decduck3.ironcomputing.item.ROMItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ROMSlot extends Slot {
    public ROMSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ROMItem;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
