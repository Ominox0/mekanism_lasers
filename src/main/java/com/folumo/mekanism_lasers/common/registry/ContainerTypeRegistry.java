package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block_entity.InterfaceBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import com.folumo.mekanism_lasers.common.container.LaserConfiguratorContainer;
import com.folumo.mekanism_lasers.common.item.LaserConfiguratorItem;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;


public class ContainerTypeRegistry {

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<OreGeneratorBlockEntity>> ORE_GENERATOR =
            CONTAINER_TYPES.custom("ore_generator", OreGeneratorBlockEntity.class).build();


    public static final ContainerTypeRegistryObject<MekanismTileContainer<InterfaceBlockEntity>> INTERFACE_BLOCK =
            CONTAINER_TYPES.custom("interface_block", InterfaceBlockEntity.class).build();

    public static final ContainerTypeRegistryObject<LaserConfiguratorContainer> LASER_CONFIGURATOR =
            CONTAINER_TYPES.register(ItemRegistry.LASER_CONFIGURATOR, LaserConfiguratorItem.class, LaserConfiguratorContainer::new);
}
