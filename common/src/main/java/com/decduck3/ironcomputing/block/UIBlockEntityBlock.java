package com.decduck3.ironcomputing.block;

import com.decduck3.ironcomputing.blockentity.Computer;
import com.decduck3.ironcomputing.blockentity.ContainerBlockEntity;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class UIBlockEntityBlock<T extends ContainerBlockEntity> extends BaseEntityBlock implements InteractionEvent.RightClickBlock {
    private final BiFunction<BlockPos, BlockState, T> blockEntitySupplier;

    public UIBlockEntityBlock(Properties properties, BiFunction<BlockPos, BlockState, T> blockEntitySupplier) {
        super(properties);
        this.blockEntitySupplier = blockEntitySupplier;

        InteractionEvent.RIGHT_CLICK_BLOCK.register(this);
    }

    @Override
    public EventResult click(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        BlockEntity entity = player.level().getBlockEntity(pos);
        if (!(entity instanceof ContainerBlockEntity blockEntity)) {
            return EventResult.pass();
        }
        if (player.isShiftKeyDown()) {
            return EventResult.pass();
        }

        player.openMenu(blockEntity);

        return EventResult.interruptTrue();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.blockEntitySupplier.apply(pos, state);
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

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
