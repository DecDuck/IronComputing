package com.decduck3.ironcomputing.blockentity;

import com.decduck3.ironcomputing.IronComputing;
import com.decduck3.ironcomputing.block.ComputerCaseBlock;
import com.decduck3.ironcomputing.block.IronComputingBlocks;
import com.mojang.datafixers.types.Type;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.intellij.lang.annotations.Identifier;

import java.util.function.Supplier;

public class IronComputingBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(IronComputing.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static RegistrySupplier<BlockEntityType<Computer>> COMPUTER_BLOCK_ENTITY_TYPE;
    public static RegistrySupplier<BlockEntityType<FirmwareStation>> FIRMWARE_STATION_ENTITY_TYPE;

    public static void initRegistry() {
        BLOCK_ENTITY_TYPES.register();
    }

    public static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, Supplier<T> blockEntity) {
        return BLOCK_ENTITY_TYPES.register(ResourceLocation.tryBuild(IronComputing.MOD_ID, name), blockEntity);
    }

}
