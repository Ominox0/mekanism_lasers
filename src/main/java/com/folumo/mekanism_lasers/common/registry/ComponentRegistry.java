package com.folumo.mekanism_lasers.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;

import java.util.List;
import java.util.function.UnaryOperator;

public class ComponentRegistry {
    public static final DataComponentType<List<BlockPos>> RC_BLOCKPOS = register(
            "rc_blockpos", builder -> builder.persistent(BlockPos.CODEC.listOf())
                    //.networkSynchronized(BeehiveBlockEntity.Occupant.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .cacheEncoding()
    );
    public static final DataComponentType<Boolean> RC_ACTIVITY = register(
            "rc_activity", builder -> builder.persistent(Codec.BOOL)
            //.networkSynchronized(ByteBufCodecs.BOOL)
                    .cacheEncoding()
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, name, builderUnaryOperator.apply(DataComponentType.builder()).build());
    }
}
