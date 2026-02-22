package com.decduck3.ironcomputing.item;

import com.decduck3.ironcomputing.tabs.IronComputingTabs;
import net.minecraft.world.item.Item;

public class RAMItem extends Item {

    public record RAMGenData(int size) {
        public String getItemId() {
            return String.format("ram_size_%s", this.size);
        }
    }

    // RAM sizes are in WASM pages (64KiB)
    public static final RAMGenData[] RAMS = new RAMGenData[]{//.
            new RAMGenData(1), //64kb
            new RAMGenData(8), //512kb
            new RAMGenData(32), //2MB
            new RAMGenData(64), //4MB
            new RAMGenData(128), //8MB
    };

    private final RAMGenData genData;

    public RAMItem(RAMGenData genData) {
        super(new Properties().arch$tab(IronComputingTabs.COMPONENTS_TAB));
        this.genData = genData;
    }
}
