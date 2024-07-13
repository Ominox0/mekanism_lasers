package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block.ToggleableLaser;
import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.AdvancedLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.EliteLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.UltimateLaserBlockEntity;
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


    public static final TileEntityTypeRegistryObject<BasicLaserBlockEntity> BASIC_LASER =
            BLOCK_ENTITY_TYPES.mekBuilder(BlockRegistry.BASIC_LASER, BasicLaserBlockEntity::new)
                    .serverTicker(TileEntityMekanism::tickServer)
                    .withSimple(Capabilities.LASER_RECEPTOR)
                    .build();
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> BASIC_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.BASIC_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ADVANCED_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ADVANCED_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ELITE_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ELITE_TOGGLEABLE_LASER);
    public static final TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> ULTIMATE_TOGGLEABLE_LASER = createToggleableLaser(BlockRegistry.ULTIMATE_TOGGLEABLE_LASER);

    private static TileEntityTypeRegistryObject<ToggleableLaserBlockEntity> createToggleableLaser(BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> block) {
        return BLOCK_ENTITY_TYPES.mekBuilder(block, (pos, state) -> new ToggleableLaserBlockEntity(block, pos, state))
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIGURABLE)
                .build();
    }


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
