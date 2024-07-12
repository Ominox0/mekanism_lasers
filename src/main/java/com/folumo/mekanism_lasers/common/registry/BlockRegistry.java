package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block.LaserStopper;
import com.folumo.mekanism_lasers.common.block.OreGenerator;
import com.folumo.mekanism_lasers.common.block.laser.AdvancedLaser;
import com.folumo.mekanism_lasers.common.block.laser.BasicLaser;
import com.folumo.mekanism_lasers.common.block.laser.EliteLaser;
import com.folumo.mekanism_lasers.common.block.laser.UltimateLaser;
import com.folumo.mekanism_lasers.common.item.LaserStopperBlockItem;
import com.folumo.mekanism_lasers.common.item.OreGeneratorBlockItem;
import com.folumo.mekanism_lasers.common.item.laser.AdvancedLaserBlockItem;
import com.folumo.mekanism_lasers.common.item.laser.BasicLaserBlockItem;
import com.folumo.mekanism_lasers.common.item.laser.EliteLaserBlockItem;
import com.folumo.mekanism_lasers.common.item.laser.UltimateLaserBlockItem;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;


public class BlockRegistry {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final BlockRegistryObject<LaserStopper, LaserStopperBlockItem> LASER_STOPPER = BLOCKS.register("laser_stopper",
            () -> new LaserStopper(BlockTypeRegistry.LASER_STOPPER), LaserStopperBlockItem::new);

    public static final BlockRegistryObject<BasicLaser, BasicLaserBlockItem> BASIC_LASER = BLOCKS.register("basic_laser",
            () -> new BasicLaser(BlockTypeRegistry.BASIC_LASER), BasicLaserBlockItem::new);

    public static final BlockRegistryObject<AdvancedLaser, AdvancedLaserBlockItem> ADVANCED_LASER = BLOCKS.register("advanced_laser",
            () -> new AdvancedLaser(BlockTypeRegistry.ADVANCED_LASER), AdvancedLaserBlockItem::new);

    public static final BlockRegistryObject<EliteLaser, EliteLaserBlockItem> ELITE_LASER = BLOCKS.register("elite_laser",
            () -> new EliteLaser(BlockTypeRegistry.ELITE_LASER), EliteLaserBlockItem::new);

    public static final BlockRegistryObject<UltimateLaser, UltimateLaserBlockItem> ULTIMATE_LASER = BLOCKS.register("ultimate_laser",
            () -> new UltimateLaser(BlockTypeRegistry.ULTIMATE_LASER), UltimateLaserBlockItem::new);

    public static final BlockRegistryObject<OreGenerator, OreGeneratorBlockItem> ORE_GENERATOR = BLOCKS.register("ore_generator",
            () -> new OreGenerator(BlockTypeRegistry.ORE_GENERATOR), OreGeneratorBlockItem::new);

}
