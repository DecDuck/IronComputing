package com.decduck3.ironcomputing.item;

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
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        CompoundTag tag = stack.getTagElement("romId");
        if (tag != null) {
            String romId = tag.getAsString();
            tooltipComponents.add(Component.literal(romId).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
