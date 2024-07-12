package com.folumo.mekanism_lasers.client;


import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.client.gui.OreGeneratorScreen;
import com.folumo.mekanism_lasers.client.renderer.LaserStopperRenderer;
import com.folumo.mekanism_lasers.common.registry.BlockEntityTypeRegistry;
import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.client.ClientRegistrationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Mekanism_lasers.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityTypeRegistry.LASER_STOPPER.get(), LaserStopperRenderer::new);
    }

    @SubscribeEvent
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(ContainerTypeRegistry.ORE_GENERATOR, OreGeneratorScreen::new);
        });
    }
}
