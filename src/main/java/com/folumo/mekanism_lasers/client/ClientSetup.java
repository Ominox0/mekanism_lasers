package com.folumo.mekanism_lasers.client;


import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.client.gui.OreGeneratorScreen;
import com.folumo.mekanism_lasers.client.gui.InterfaceBlockScreen;
import com.folumo.mekanism_lasers.client.gui.LaserConfiguratorItemScreen;
import com.folumo.mekanism_lasers.client.renderer.LaserStopperRenderer;
import com.folumo.mekanism_lasers.common.registry.BlockEntityTypeRegistry;
import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.client.ClientRegistrationUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;


@EventBusSubscriber(modid = Mekanism_lasers.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.LASER_STOPPER.get(), LaserStopperRenderer::new);
    }

    @SubscribeEvent
    public static void registerContainers(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegistry.ORE_GENERATOR, OreGeneratorScreen::new);
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegistry.INTERFACE_BLOCK, InterfaceBlockScreen::new);
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegistry.LASER_CONFIGURATOR, LaserConfiguratorItemScreen::new);
    }
}
