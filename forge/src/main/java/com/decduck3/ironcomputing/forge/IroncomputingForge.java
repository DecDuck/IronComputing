package com.decduck3.ironcomputing.forge;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.forge.blockentity.IronComputingBlockEntitiesForge;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IronComputing.MOD_ID)
public final class IroncomputingForge {
    public IroncomputingForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(IronComputing.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        IronComputing.init();

        IronComputingBlockEntitiesForge.init();
    }
}
