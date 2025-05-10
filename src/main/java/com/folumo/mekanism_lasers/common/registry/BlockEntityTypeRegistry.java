package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block.Laser;
import com.folumo.mekanism_lasers.common.block.ToggleableLaser;
import com.folumo.mekanism_lasers.common.block_entity.*;
import com.folumo.mekanism_lasers.common.item.LaserBlockItem;
import com.folumo.mekanism_lasers.common.item.ToggleableLaserBlockItem;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class BlockEntityTypeRegistry {

    public static final TileEntityTypeDeferredRegister BLOCK_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final TileEntityTypeRegistryObject<LaserStopperBlockEntity> LASER_STOPPER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.LASER_STOPPER, LaserStopperBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .withSimple(Capabilities.CONFIGURABLE)
                    .build();

    public static final TileEntityTypeRegistryObject<LaserSplitterBlockEntity> LASER_SPLITTER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.LASER_SPLITTER, LaserSplitterBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .withSimple(Capabilities.CONFIGURABLE)
                    .build();

    public static final TileEntityTypeRegistryObject<EnergyTransformerBlockEntity> ENERGY_TRANSFORMER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ENERGY_TRANSFORMER, EnergyTransformerBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .withSimple(Capabilities.CONFIGURABLE)
                    .build();

    public static final TileEntityTypeRegistryObject<LaserBlockEntity> BASIC_LASER = createLaser(BlockRegistry.BASIC_LASER);
    public static final TileEntityTypeRegistryObject<LaserBlockEntity> ADVANCED_LASER = createLaser(BlockRegistry.ADVANCED_LASER);
    public static final TileEntityTypeRegistryObject<LaserBlockEntity> ELITE_LASER = createLaser(BlockRegistry.ELITE_LASER);
    public static final TileEntityTypeRegistryObject<LaserBlockEntity> ULTIMATE_LASER = createLaser(BlockRegistry.ULTIMATE_LASER);
    public static final TileEntityTypeRegistryObject<LaserBlockEntity> CREATIVE_LASER = createLaser(BlockRegistry.CREATIVE_LASER);

    private static TileEntityTypeRegistryObject<LaserBlockEntity> createLaser(BlockRegistryObject<Laser, LaserBlockItem> block) {
        return BLOCK_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new LaserBlockEntity(block, pos, state))
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.LASER_RECEPTOR)
                .build();
    }

    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> BASIC_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.BASIC_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ADVANCED_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ADVANCED_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ELITE_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ELITE_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ULTIMATE_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ULTIMATE_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> CREATIVE_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.CREATIVE_TOGGLEABLE_LASER);

    private static TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> createToggleableLaser(BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> block) {
        return BLOCK_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new ToggleableLaserBlockEntity(block, pos, state))
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();
    }

    public static final TileEntityTypeRegistryObject<OreGeneratorBlockEntity> ORE_GENERATOR =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ORE_GENERATOR, OreGeneratorBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<InterfaceBlockEntity> INTERFACE_BLOCK =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.INTERFACE_BLOCK, InterfaceBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .build();

    public static final TileEntityTypeRegistryObject<EnergyStorageCasingBlockEntity> ENERGY_STORAGE_CASING =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ENERGY_STORAGE_CASING, (blockPos, state) -> new EnergyStorageCasingBlockEntity(BlockRegistry.ENERGY_STORAGE_CASING, blockPos, state))
                    .clientTicker(TileEntityMekanism::tickClient)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.CONFIGURABLE)
    //Disable item handler caps if we are the induction casing (but not the port)
                    .without(Capabilities.ITEM.block())
                    .build();

    public static final TileEntityTypeRegistryObject<EnergyStoragePortBlockEntity> ENERGY_STORAGE_PORT =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ENERGY_STORAGE_PORT, (blockPos, state) -> new  EnergyStoragePortBlockEntity(BlockRegistry.ENERGY_STORAGE_PORT, blockPos, state))
                    .clientTicker(TileEntityMekanism::tickClient)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .withSimple(Capabilities.CONFIGURABLE)
                    .build();


    public static final TileEntityTypeRegistryObject<EnergyStorageCellBlockEntity> ENERGY_STORAGE_CELL =
            BLOCK_ENTITY_TYPES.builder(BlockRegistry.ENERGY_STORAGE_CELL, (pos, state) -> new EnergyStorageCellBlockEntity(BlockRegistry.ENERGY_STORAGE_CELL, pos, state)).build();
}
