package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.AdvancedLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.EliteLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.UltimateLaserBlockEntity;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class BlockEntityTypeRegistry {

    public static final TileEntityTypeDeferredRegister BLOCK_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final TileEntityTypeRegistryObject<LaserStopperBlockEntity> LASER_STOPPER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.LASER_STOPPER, LaserStopperBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<BasicLaserBlockEntity> BASIC_LASER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.BASIC_LASER, BasicLaserBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<AdvancedLaserBlockEntity> ADVANCED_LASER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ADVANCED_LASER, AdvancedLaserBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<EliteLaserBlockEntity> ELITE_LASER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ELITE_LASER, EliteLaserBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<UltimateLaserBlockEntity> ULTIMATE_LASER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ULTIMATE_LASER, UltimateLaserBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();

    public static final TileEntityTypeRegistryObject<OreGeneratorBlockEntity> ORE_GENERATOR =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.ORE_GENERATOR, OreGeneratorBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();
}
