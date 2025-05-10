package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.IContentsListener;
import mekanism.api.functions.ConstantPredicates;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EnergyTransformerBlockEntity extends TileEntityLaserReceptor {

    public EnergyTransformerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ENERGY_TRANSFORMER, pos, state);
    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), this, listener));
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Energy Transformer");
    }

    @Override
    protected boolean onUpdateServer() {
        return false;
    }
}

