package com.decduck3.ironcomputing.block;

import com.decduck3.ironcomputing.IronComputing;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class IronComputingBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IronComputing.MOD_ID, Registries.BLOCK);

    public static RegistrySupplier<ComputerCaseBlock> COMPUTER_CASE_BLOCK;

    public static void init() {
        COMPUTER_CASE_BLOCK = registerBlock("computer_case", ComputerCaseBlock::new);

        BLOCKS.register();
    }

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(ResourceLocation.tryBuild(IronComputing.MOD_ID, name), block);
    }

    public static BlockBehaviour.Properties baseProperties() {
        return BlockBehaviour.Properties.of();
    }
}
