package com.decduck3.ironcomputing.state;

import com.decduck3.ironcomputing.gui.slot.CPUSlot;
import com.decduck3.ironcomputing.gui.slot.RAMSlot;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public record CCInvLayout(CCType type, int x, int y) {
    public @NotNull Slot createSlot(Container container, int index){
        switch(this.type) {
            case CPU -> {
                return new CPUSlot(container, index, this.x, this.y);
            }
            case RAM -> {
                return new RAMSlot(container, index, this.x, this.y);
            }
        };

        return new Slot(container, index, this.x, this.y);
    }
}
