package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;


public class ContainerTypeRegistry {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<OreGeneratorBlockEntity>> ORE_GENERATOR =
            CONTAINER_TYPES.custom("ore_generator", OreGeneratorBlockEntity.class).build();

    //public static final ContainerTypeRegistryObject<LaserConfiguratorContainer> LASER_CONFIGURATOR =
    //        CONTAINER_TYPES.register(ItemRegistry.LASER_CONFIGURATOR, LaserConfiguratorItem.class, LaserConfiguratorContainer::new);

    //public static final ContainerTypeRegistryObject<PortableTeleporterContainer> PORTABLE_TELEPORTER =
    //        CONTAINER_TYPES.register(MekanismItems.PORTABLE_TELEPORTER, ItemPortableTeleporter.class, PortableTeleporterContainer::new);
}
