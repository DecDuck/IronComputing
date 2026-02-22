package com.decduck3.ironcomputing.tabs;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.item.IronComputingItems;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class IronComputingTabs {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(IronComputing.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static RegistrySupplier<CreativeModeTab> COMPONENTS_TAB;

    public static void init() {
        COMPONENTS_TAB = TABS.register("components", () -> CreativeTabRegistry.create(Component.translatable("tabs.ironcomputing.components"), () -> IronComputingItems.COMPUTER_CASE_BLOCKITEM.get().getDefaultInstance()));

        TABS.register();
    }

}
