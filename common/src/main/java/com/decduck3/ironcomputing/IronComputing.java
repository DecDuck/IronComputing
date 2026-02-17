package com.decduck3.ironcomputing;

import com.decduck3.ironcomputing.block.IronComputingBlocks;
import com.decduck3.ironcomputing.ironserver.IronServer;
import com.decduck3.ironcomputing.item.IronComputingItems;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IronComputing {
    public static final String MOD_ID = "ironcomputing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
        LOGGER.info("IronComputing starting up...");

        IronComputingBlocks.init();
        IronComputingItems.init();

        LifecycleEvent.SERVER_STARTING.register(minecraftServer -> IronServer.INSTANCE = IronServer.createIronServer());
    }
}
