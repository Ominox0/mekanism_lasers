package com.folumo.mekanism_lasers.common.container;

import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class LaserConfiguratorContainer extends MekanismItemContainer {

    public LaserConfiguratorContainer(int id, Inventory inv, InteractionHand hand, ItemStack stack) {
        super(ContainerTypeRegistry.LASER_CONFIGURATOR, id, inv, hand, stack);

        //itemInventory = isRemote ? new ClientSidePersonalStorageInventory() : PersonalStorageManager.getInventoryFor(stack).orElseThrow(() -> new IllegalStateException("Inventory not available"));
    }

    //@Override
    //protected void addSlots() {
    //    super.addSlots();
    //    //Get all the inventory slots the tile has
    //    List<IInventorySlot> inventorySlots = itemInventory.getInventorySlots(null);
    //    for (IInventorySlot inventorySlot : inventorySlots) {
    //        Slot containerSlot = inventorySlot.createContainerSlot();
    //        if (containerSlot != null) {
    //            addSlot(containerSlot);
    //        }
    //    }
    //}
}
