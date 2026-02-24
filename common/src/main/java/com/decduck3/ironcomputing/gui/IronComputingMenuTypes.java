package com.decduck3.ironcomputing.gui;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.gui.computer.ComputerMenu;
import com.decduck3.ironcomputing.gui.computer.ComputerScreen;
import com.decduck3.ironcomputing.gui.firmware.FirmwareStationMenu;
import com.decduck3.ironcomputing.gui.firmware.FirmwareStationScreen;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class IronComputingMenuTypes {

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(IronComputing.MOD_ID, Registries.MENU);

    public static RegistrySupplier<MenuType<ComputerMenu>> COMPUTER;
    public static RegistrySupplier<MenuType<FirmwareStationMenu>> FIRMWARE_STATION;

    public static void init() {
        COMPUTER = register("computer", () -> new MenuType<>(ComputerMenu::new, FeatureFlagSet.of()));
        FIRMWARE_STATION = register("firmware_station", () -> new MenuType<>(FirmwareStationMenu::new, FeatureFlagSet.of()));

        MENU_TYPES.register();

        if (Platform.getEnv() == EnvType.CLIENT) {
            ClientLifecycleEvent.CLIENT_STARTED.register(client -> {
                MenuRegistry.registerScreenFactory(COMPUTER.get(), ComputerScreen::new);
                MenuRegistry.registerScreenFactory(FIRMWARE_STATION.get(), FirmwareStationScreen::new);
            });
        }
    }


    public static <T extends MenuType<?>> RegistrySupplier<T> register(String name, Supplier<T> menuType) {
        return MENU_TYPES.register(ResourceLocation.tryBuild(IronComputing.MOD_ID, name), menuType);
    }
}
