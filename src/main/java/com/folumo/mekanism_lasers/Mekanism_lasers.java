package com.folumo.mekanism_lasers;

import com.folumo.mekanism_lasers.common.multiblock.data.energyStorageMultiblockData;
import com.folumo.mekanism_lasers.common.multiblock.validator.energyStorageMultiblockValidator;
import com.folumo.mekanism_lasers.common.registry.*;
import com.mojang.logging.LogUtils;
import mekanism.common.lib.multiblock.MultiblockManager;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import mekanism.common.lib.multiblock.MultiblockCache;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;


@Mod(com.folumo.mekanism_lasers.Mekanism_lasers.MOD_ID)
public class Mekanism_lasers {

    public static final String MOD_ID = "mekanism_lasers";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MultiblockManager<energyStorageMultiblockData> energyStorage = new MultiblockManager<>("energyStorage", MultiblockCache::new, energyStorageMultiblockValidator::new);

    public Mekanism_lasers(IEventBus modEventBus, ModContainer modContainer){
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        ContainerTypeRegistry.CONTAINER_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);


        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}


/* TODO:
 *  new features/modification in next update:
 *  1. New laser type (Toggleable Laser) -> Done
 *  2. Remote Control to turn on/off Toggleable Lasers -> in progress
 *  3. Remote Interface (you have to put Remove Control in it to work) -> queened
 *      -Takes redstone signal and based on signal it will turn on/off Toggleable Lasers
 *  4. Make Ore Generator be able to rotate -> Done
 *  5. Laser Configurator -> queened
 *      -Make it so that you can open gui for any laser except mekanism:laser
 *      -Empty gui with only title
 */