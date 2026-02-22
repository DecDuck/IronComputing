package com.decduck3.ironcomputing.item;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.block.ComputerCaseBlock;
import com.decduck3.ironcomputing.tabs.IronComputingTabs;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static com.decduck3.ironcomputing.block.IronComputingBlocks.COMPUTER_CASE_BLOCK;

public class IronComputingItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IronComputing.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> COMPUTER_CASE_BLOCKITEM;

    public static void init() {
        COMPUTER_CASE_BLOCKITEM = registerItem("computer_case", () -> new BlockItem(COMPUTER_CASE_BLOCK.get(), baseProperties().arch$tab(IronComputingTabs.COMPONENTS_TAB)));

        for(CPUItem.CPUGenData cpuData : CPUItem.CPUS) {
            registerItem(cpuData.getItemId(), () -> new CPUItem(cpuData));
        }
        for(RAMItem.RAMGenData ramData : RAMItem.RAMS) {
            registerItem(ramData.getItemId(), () -> new RAMItem(ramData));
        }

        ITEMS.register();
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(ResourceLocation.tryBuild(IronComputing.MOD_ID, name), item);
    }

    public static Item.Properties baseProperties() {
        return new Item.Properties();
    }
}
