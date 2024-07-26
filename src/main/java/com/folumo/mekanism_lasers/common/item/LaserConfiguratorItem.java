package com.folumo.mekanism_lasers.common.item;


import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.lib.inventory.personalstorage.ClientSidePersonalStorageInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class LaserConfiguratorItem extends Item implements IGuiItem {


    public LaserConfiguratorItem(Properties properties) {
        super(properties);
    }

    @Override
    public ContainerTypeRegistryObject<?> getContainerType() {
        return null;
    }
}
