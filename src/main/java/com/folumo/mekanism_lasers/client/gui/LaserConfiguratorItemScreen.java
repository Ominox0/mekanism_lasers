package com.folumo.mekanism_lasers.client.gui;

import com.folumo.mekanism_lasers.common.container.LaserConfiguratorContainer;
import com.folumo.mekanism_lasers.common.item.LaserConfiguratorItem;
import mekanism.client.gui.GuiMekanism;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class LaserConfiguratorItemScreen extends GuiMekanism<LaserConfiguratorContainer> {
    protected LaserConfiguratorItemScreen(LaserConfiguratorContainer laserConfiguratorItem, Inventory inv, Component title) {
        super(laserConfiguratorItem, inv, title);
    }
}
