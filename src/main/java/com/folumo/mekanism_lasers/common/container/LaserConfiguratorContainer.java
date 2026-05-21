package com.folumo.mekanism_lasers.common.container;

import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class LaserConfiguratorContainer extends MekanismItemContainer {

    public LaserConfiguratorContainer(int id, Inventory inv, InteractionHand hand, ItemStack stack) {
        super(ContainerTypeRegistry.LASER_CONFIGURATOR, id, inv, hand, stack);
    }
}
