package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.block.*;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.folumo.mekanism_lasers.common.block_entity.*;
import com.folumo.mekanism_lasers.common.item.*;
import mekanism.common.block.prefab.BlockTile;
import mekanism.api.tier.ITier;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;


public class BlockRegistry {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Mekanism_lasers.MOD_ID);

    public static final BlockRegistryObject<LaserStopper, LaserStopperBlockItem> LASER_STOPPER = BLOCKS.register("laser_stopper",
            () -> new LaserStopper(BlockTypeRegistry.LASER_STOPPER), LaserStopperBlockItem::new);

    public static final BlockRegistryObject<LaserSplitter, LaserSplitterBlockItem> LASER_SPLITTER = BLOCKS.register("laser_splitter",
            () -> new LaserSplitter(BlockTypeRegistry.LASER_SPLITTER), LaserSplitterBlockItem::new);

    public static final BlockRegistryObject<EnergyTransformer, EnergyTransformerBlockItem> ENERGY_TRANSFORMER = BLOCKS.register("energy_transformer",
            () -> new EnergyTransformer(BlockTypeRegistry.ENERGY_TRANSFORMER), EnergyTransformerBlockItem::new);

    public static final BlockRegistryObject<Laser, LaserBlockItem> BASIC_LASER = registerLaser(BlockTypeRegistry.BASIC_LASER);
    public static final BlockRegistryObject<Laser, LaserBlockItem> ADVANCED_LASER = registerLaser(BlockTypeRegistry.ADVANCED_LASER);
    public static final BlockRegistryObject<Laser, LaserBlockItem> ELITE_LASER = registerLaser(BlockTypeRegistry.ELITE_LASER);
    public static final BlockRegistryObject<Laser, LaserBlockItem> ULTIMATE_LASER = registerLaser(BlockTypeRegistry.ULTIMATE_LASER);
    public static final BlockRegistryObject<Laser, LaserBlockItem> CREATIVE_LASER = registerLaser(BlockTypeRegistry.CREATIVE_LASER);

    private static BlockRegistryObject<Laser, LaserBlockItem> registerLaser(BlockTypeTile<LaserBlockEntity> type) {
        return registerTieredBlock(type, "_laser", () -> new Laser(type), LaserBlockItem::new);
    }

    public static final BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> BASIC_TOGGLEABLE_LASER = registerToggleableLaser(BlockTypeRegistry.BASIC_TOGGLEABLE_LASER);
    public static final BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> ADVANCED_TOGGLEABLE_LASER = registerToggleableLaser(BlockTypeRegistry.ADVANCED_TOGGLEABLE_LASER);
    public static final BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> ELITE_TOGGLEABLE_LASER = registerToggleableLaser(BlockTypeRegistry.ELITE_TOGGLEABLE_LASER);
    public static final BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> ULTIMATE_TOGGLEABLE_LASER = registerToggleableLaser(BlockTypeRegistry.ULTIMATE_TOGGLEABLE_LASER);
    public static final BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> CREATIVE_TOGGLEABLE_LASER = registerToggleableLaser(BlockTypeRegistry.CREATIVE_TOGGLEABLE_LASER);

    private static BlockRegistryObject<ToggleableLaser, ToggleableLaserBlockItem> registerToggleableLaser(BlockTypeTile<ToggleableLaserBlockEntity> type) {
        return registerTieredBlock(type, "_toggleable_laser", () -> new ToggleableLaser(type), ToggleableLaserBlockItem::new);
    }

    public static final BlockRegistryObject<OreGenerator, OreGeneratorBlockItem> ORE_GENERATOR = BLOCKS.register("ore_generator",
            () -> new OreGenerator(BlockTypeRegistry.ORE_GENERATOR), OreGeneratorBlockItem::new);


    public static final BlockRegistryObject<InterfaceBlock, InterfaceBlockItem> INTERFACE_BLOCK = BLOCKS.register("interface_block",
            () -> new InterfaceBlock(BlockTypeRegistry.INTERFACE_BLOCK), InterfaceBlockItem::new);


    public static final BlockRegistryObject<BlockTile<EnergyStorageCasingBlockEntity, BlockTypeTile<EnergyStorageCasingBlockEntity>>, BlockItem> ENERGY_STORAGE_CASING =
            BLOCKS.register("energy_storage_casing", () -> new BlockTile<>(BlockTypeRegistry.ENERGY_STORAGE_CASING, properties -> properties.mapColor(MapColor.COLOR_LIGHT_GRAY)), BlockItem::new);

    public static final BlockRegistryObject<BlockTile<EnergyStoragePortBlockEntity, BlockTypeTile<EnergyStoragePortBlockEntity>>, BlockItem> ENERGY_STORAGE_PORT =
            BLOCKS.register("energy_storage_port", () -> new BlockTile<>(BlockTypeRegistry.ENERGY_STORAGE_PORT, properties -> properties.mapColor(MapColor.COLOR_LIGHT_GRAY)), BlockItem::new);

    public static final BlockRegistryObject<BlockTile<EnergyStorageCellBlockEntity, BlockTypeTile<EnergyStorageCellBlockEntity>>, BlockItem> ENERGY_STORAGE_CELL =
        BLOCKS.register("energy_storage_cell", () -> new BlockTile<>(BlockTypeRegistry.ENERGY_STORAGE_CELL, properties -> properties.mapColor(MapColor.COLOR_LIGHT_GRAY)), BlockItem::new);




    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

}
