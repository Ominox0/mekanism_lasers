package com.folumo.mekanism_lasers;

import com.folumo.mekanism_lasers.client.ClientSetup;
import com.folumo.mekanism_lasers.common.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;


@Mod(com.folumo.mekanism_lasers.Mekanism_lasers.MOD_ID)
public class Mekanism_lasers {

    public static final String MOD_ID = "mekanism_lasers";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Mekanism_lasers(IEventBus modEventBus, ModContainer modContainer){
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        ContainerTypeRegistry.CONTAINER_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
