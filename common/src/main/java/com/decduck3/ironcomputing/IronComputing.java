package com.decduck3.ironcomputing;

import com.decduck3.ironcomputing.block.IronComputingBlocks;
import com.decduck3.ironcomputing.gui.IronComputingMenuTypes;
import com.decduck3.ironcomputing.ironserver.IronServer;
import com.decduck3.ironcomputing.item.IronComputingItems;
import com.decduck3.ironcomputing.network.IronComputingNetwork;
import com.decduck3.ironcomputing.tabs.IronComputingTabs;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class IronComputing {
    public static final String MOD_ID = "ironcomputing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final String CLIENT_THREAD_NAME = "Render thread";

    public static void init() {
        // Write common init code here.
        LOGGER.info("IronComputing starting up...");

        IronComputingTabs.init();
        IronComputingBlocks.init();
        IronComputingItems.init();
        IronComputingMenuTypes.init();
        IronComputingNetwork.init();

        LifecycleEvent.SERVER_STARTING.register(minecraftServer -> IronServer.INSTANCE = IronServer.createIronServer());
        LifecycleEvent.SERVER_STOPPING.register(minecraftServer -> IronServer.INSTANCE.destroy());
        if (Platform.getEnv() == EnvType.CLIENT) {
            ClientLifecycleEvent.CLIENT_STARTED.register(minecraft -> System.setProperty("java.awt.headless", "false"));
        }
    }

    public static boolean isServer() {
        return !Objects.equals(Thread.currentThread().getName(), CLIENT_THREAD_NAME);
    }
}
