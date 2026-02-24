package com.decduck3.ironcomputing.item;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.tabs.IronComputingTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ROMItem extends Item {
    public ROMItem() {
        super(IronComputingItems.baseProperties().arch$tab(IronComputingTabs.COMPONENTS_TAB));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            String romId = tag.getString("romId");
            tooltipComponents.add(Component.literal(romId).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
