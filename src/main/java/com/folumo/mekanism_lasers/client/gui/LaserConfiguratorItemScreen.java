package com.folumo.mekanism_lasers.client.gui;

import com.folumo.mekanism_lasers.common.container.LaserConfiguratorContainer;
import mekanism.client.gui.GuiMekanism;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class LaserConfiguratorItemScreen extends GuiMekanism<LaserConfiguratorContainer> {
    public LaserConfiguratorItemScreen(LaserConfiguratorContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();

    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
