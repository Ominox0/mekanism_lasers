package com.folumo.mekanism_lasers.common.container;

import mekanism.common.inventory.container.item.MekanismItemContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class LaserConfiguratorContainer extends MekanismItemContainer {
    public LaserConfiguratorContainer(ContainerTypeRegistryObject<?> type, int id, Inventory inv, InteractionHand hand, ItemStack stack) {
        super(type, id, inv, hand, stack);
    }
}
