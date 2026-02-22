package com.decduck3.ironcomputing.gui;

import com.decduck3.ironcomputing.IronComputing;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class IronComputingMenuTypes {

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(IronComputing.MOD_ID, Registries.MENU);

    public static RegistrySupplier<MenuType<ComputerMenu>> COMPUTER;

    public static void init() {
        COMPUTER = register("computer", () -> new MenuType<>(ComputerMenu::new, FeatureFlagSet.of()));

        MENU_TYPES.register();

        ClientLifecycleEvent.CLIENT_STARTED.register(client -> MenuRegistry.registerScreenFactory(COMPUTER.get(), ComputerScreen::new));
    }

    public static <T extends MenuType<?>> RegistrySupplier<T> register(String name, Supplier<T> menuType) {
        return MENU_TYPES.register(ResourceLocation.tryBuild(IronComputing.MOD_ID, name), menuType);
    }
}
