package com.decduck3.ironcomputing.item;

import com.decduck3.ironcomputing.tabs.IronComputingTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CPUItem extends Item {
    public enum CPUModel {
        Minimal, WasiP1, WasiP2,
    }


    public record CPUGenData(CPUModel model, String target) {
        public String getItemId() {
            return String.format("cpu_model_%s", this.model.name().toLowerCase());
        }
    }

    public static final CPUGenData[] CPUS = new CPUGenData[]{ //.
            new CPUGenData(CPUModel.Minimal, "wasm32-unknown-unknown"), //.
            new CPUGenData(CPUModel.WasiP1, "wasm32-wasip1"), //.
            new CPUGenData(CPUModel.WasiP2, "wasm32-wasip2") //.
    };

    private final CPUGenData genData;

    public CPUItem(CPUGenData genData) {
        super(new Properties().arch$tab(IronComputingTabs.COMPONENTS_TAB));
        this.genData = genData;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isAdvanced.isAdvanced()) {
            tooltipComponents.add(Component.literal(this.genData.target).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
