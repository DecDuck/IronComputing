package com.decduck3.ironcomputing.fabric;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.fabric.blockentity.IronComputingBlockEntitiesFabric;
import net.fabricmc.api.ModInitializer;

public final class IroncomputingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        IronComputing.init();

        IronComputingBlockEntitiesFabric.init();
    }
}
