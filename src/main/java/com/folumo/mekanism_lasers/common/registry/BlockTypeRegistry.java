package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.common.block_entity.*;
import com.folumo.mekanism_lasers.common.lang.MekanismLasersLang;
import com.folumo.mekanism_lasers.common.tier.LaserTier;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismSounds;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

import static com.folumo.mekanism_lasers.common.block.BlockShapes.LASER_STOPPER_SHAPE;
import static com.folumo.mekanism_lasers.common.block.BlockShapes.ORE_GENERATOR_SHAPE;
import static com.folumo.mekanism_lasers.common.block.BlockShapes.ENERGY_STORAGE_CELL_SHAPE;

public class BlockTypeRegistry {
    public static final BlockTypeTile<LaserStopperBlockEntity> LASER_STOPPER = BlockTypeTile.BlockTileBuilder.createBlock(() -> BlockEntityTypeRegistry.LASER_STOPPER, MekanismLasersLang.DESCRIPTION_LASER_STOPPER)
            .withCustomShape(LASER_STOPPER_SHAPE)
            .without(AttributeParticleFX.class, AttributeStateFacing.class, Attributes.AttributeRedstone.class)
            .build();
    public static final BlockTypeTile<LaserSplitterBlockEntity> LASER_SPLITTER = BlockTypeTile.BlockTileBuilder.createBlock(() -> BlockEntityTypeRegistry.LASER_SPLITTER, MekanismLasersLang.DESCRIPTION_LASER_SPLITTER)
            .withEnergyConfig(() -> LaserSplitterBlockEntity.getMaxEnergy() / 2, LaserSplitterBlockEntity::getMaxEnergy)
            .without(AttributeParticleFX.class, Attributes.AttributeRedstone.class)
            .with(Attributes.ACTIVE, new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE))
            .build();

    public static final BlockTypeTile<EnergyTransformerBlockEntity> ENERGY_TRANSFORMER = BlockTypeTile.BlockTileBuilder.createBlock(() -> BlockEntityTypeRegistry.ENERGY_TRANSFORMER, MekanismLasersLang.ENERGY_TRANSFORMER)
            .withEnergyConfig(() -> 8_000_000_000L, () -> 8_000_000_000L)
            .without(AttributeParticleFX.class, Attributes.AttributeRedstone.class)
            .with(Attributes.ACTIVE, new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE))
            .build();

    public static final BlockTypeTile<LaserBlockEntity> BASIC_LASER = createLaser(LaserTier.BASIC, () -> BlockEntityTypeRegistry.BASIC_LASER);
    public static final BlockTypeTile<LaserBlockEntity> ADVANCED_LASER = createLaser(LaserTier.ADVANCED, () -> BlockEntityTypeRegistry.ADVANCED_LASER);
    public static final BlockTypeTile<LaserBlockEntity> ELITE_LASER = createLaser(LaserTier.ELITE, () -> BlockEntityTypeRegistry.ELITE_LASER);
    public static final BlockTypeTile<LaserBlockEntity> ULTIMATE_LASER = createLaser(LaserTier.ULTIMATE, () -> BlockEntityTypeRegistry.ULTIMATE_LASER);
    public static final BlockTypeTile<LaserBlockEntity> CREATIVE_LASER = createLaser(LaserTier.CREATIVE, () -> BlockEntityTypeRegistry.CREATIVE_LASER);

    private static <TILE extends LaserBlockEntity> BlockTypeTile<TILE> createLaser(LaserTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile) {
        return BlockTypeTile.BlockTileBuilder
                .createBlock(tile, MekanismLasersLang.DESCRIPTION_LASER)
                .with(new AttributeTier<>(tier), new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE), Attributes.SECURITY)
                .withEnergyConfig(tier::getEnergyUsage, tier::getEnergyCap)
                .withCustomShape(BlockShapes.LASER)
                .withSound(MekanismSounds.LASER)
                .build();
    }

    public static final BlockTypeTile<ToggleableLaserBlockEntity> BASIC_TOGGLEABLE_LASER = createToggleableLaser(LaserTier.BASIC, () -> BlockEntityTypeRegistry.BASIC_TOGGLEABLE_LASER);
    public static final BlockTypeTile<ToggleableLaserBlockEntity> ADVANCED_TOGGLEABLE_LASER = createToggleableLaser(LaserTier.ADVANCED, () -> BlockEntityTypeRegistry.ADVANCED_TOGGLEABLE_LASER);
    public static final BlockTypeTile<ToggleableLaserBlockEntity> ELITE_TOGGLEABLE_LASER = createToggleableLaser(LaserTier.ELITE, () -> BlockEntityTypeRegistry.ELITE_TOGGLEABLE_LASER);
    public static final BlockTypeTile<ToggleableLaserBlockEntity> ULTIMATE_TOGGLEABLE_LASER = createToggleableLaser(LaserTier.ULTIMATE, () -> BlockEntityTypeRegistry.ULTIMATE_TOGGLEABLE_LASER);
    public static final BlockTypeTile<ToggleableLaserBlockEntity> CREATIVE_TOGGLEABLE_LASER = createToggleableLaser(LaserTier.CREATIVE, () -> BlockEntityTypeRegistry.CREATIVE_TOGGLEABLE_LASER);

    private static <TILE extends ToggleableLaserBlockEntity> BlockTypeTile<TILE> createToggleableLaser(LaserTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile) {
        return BlockTypeTile.BlockTileBuilder
                .createBlock(tile, MekanismLasersLang.DESCRIPTION_LASER_TOGGLEABLE)
                .with(new AttributeTier<>(tier), new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE), Attributes.SECURITY)
                .withEnergyConfig(tier::getEnergyUsage, tier::getEnergyCap)
                .withCustomShape(BlockShapes.LASER)
                .withSound(MekanismSounds.LASER)
                .build();
    }

    public static final BlockTypeTile<OreGeneratorBlockEntity> ORE_GENERATOR = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> BlockEntityTypeRegistry.ORE_GENERATOR, MekanismLasersLang.DESCRIPTION_ORE_GENERATOR)
            .withEnergyConfig(OreGeneratorBlockEntity::getUsage, OreGeneratorBlockEntity::getEnergyCap)
            .withSound(MekanismSounds.LASER)
            .withGui(() -> ContainerTypeRegistry.ORE_GENERATOR)
            .with(Attributes.ACTIVE, new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE), Attributes.SECURITY)
            .withCustomShape(ORE_GENERATOR_SHAPE)
            .build();

    public static final BlockTypeTile<InterfaceBlockEntity> INTERFACE_BLOCK = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> BlockEntityTypeRegistry.INTERFACE_BLOCK, MekanismLasersLang.DESCRIPTION_INTERFACE_BLOCK)
            .withGui(() -> ContainerTypeRegistry.INTERFACE_BLOCK)
            .with(Attributes.ACTIVE, new AttributeStateFacing(BlockStateProperties.FACING, AttributeStateFacing.FacePlacementType.SELECTED_FACE), Attributes.SECURITY)
            .build();



    public static final BlockTypeTile<EnergyStorageCasingBlockEntity> ENERGY_STORAGE_CASING = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> BlockEntityTypeRegistry.ENERGY_STORAGE_CASING, MekanismLasersLang.DESCRIPTION_ENERGY_STORAGE_CASING)
            //.withGui(() -> MekanismContainerTypes.INDUCTION_MATRIX, MekanismLang.MATRIX)
            .with(Attributes.INVENTORY, Attributes.COMPARATOR)
            .externalMultiblock()
            .build();

    public static final BlockTypeTile<EnergyStoragePortBlockEntity> ENERGY_STORAGE_PORT = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> BlockEntityTypeRegistry.ENERGY_STORAGE_PORT, MekanismLasersLang.DESCRIPTION_ENERGY_STORAGE_PORT)
            //.withGui(() -> MekanismContainerTypes.INDUCTION_MATRIX, MekanismLang.MATRIX)
            .with(Attributes.INVENTORY, Attributes.COMPARATOR, Attributes.ACTIVE)
            .externalMultiblock()
            //.withComputerSupport("inductionPort")
            .build();

    public static final BlockTypeTile<EnergyStorageCellBlockEntity> ENERGY_STORAGE_CELL =
            BlockTypeTile.BlockTileBuilder.createBlock(() -> BlockEntityTypeRegistry.ENERGY_STORAGE_CELL, MekanismLasersLang.DESCRIPTION_ENERGY_STORAGE_CELL)
                    .withCustomShape(ENERGY_STORAGE_CELL_SHAPE)
                    .withEnergyConfig(EnergyStorageCellBlockEntity::getMaxEnergy)
                    .internalMultiblock()
                    .build();

}
