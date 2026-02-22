package com.decduck3.ironcomputing.block;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.blockentity.Computer;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ComputerCaseBlock extends BaseEntityBlock implements InteractionEvent.RightClickBlock {
    public ComputerCaseBlock() {
        super(IronComputingBlocks.baseProperties());

        InteractionEvent.RIGHT_CLICK_BLOCK.register(this);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Computer(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public EventResult click(Player player, InteractionHand interactionHand, BlockPos blockPos, Direction direction) {
        BlockEntity entity = player.level().getBlockEntity(blockPos);
        if (!(entity instanceof Computer blockEntity)) {
            return EventResult.pass();
        }
        if (player.isShiftKeyDown()) {
            return EventResult.pass();
        }

        player.openMenu(blockEntity);

        return EventResult.interruptTrue();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof Computer blockEntity)) {
            return;
        }

        Containers.dropContents(level, pos, blockEntity);

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
