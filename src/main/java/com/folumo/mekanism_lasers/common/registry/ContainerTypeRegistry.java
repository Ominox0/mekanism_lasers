package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.AdvancedLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.EliteLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser.UltimateLaserBlockEntity;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class ContainerTypeRegistry {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<LaserStopperBlockEntity>> LASER_STOPPER =
            CONTAINER_TYPES.custom("laser_stopper", LaserStopperBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<MekanismTileContainer<BasicLaserBlockEntity>> BASIC_LASER =
            CONTAINER_TYPES.custom("basic_laser", BasicLaserBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<MekanismTileContainer<AdvancedLaserBlockEntity>> ADVANCED_LASER =
            CONTAINER_TYPES.custom("advanced_laser", AdvancedLaserBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<MekanismTileContainer<EliteLaserBlockEntity>> ELITE_LASER =
            CONTAINER_TYPES.custom("elite_laser", EliteLaserBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<MekanismTileContainer<UltimateLaserBlockEntity>> ULTIMATE_LASER =
            CONTAINER_TYPES.custom("ultimate_laser", UltimateLaserBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<MekanismTileContainer<OreGeneratorBlockEntity>> ORE_GENERATOR =
            CONTAINER_TYPES.custom("ore_generator", OreGeneratorBlockEntity.class).build();


}
