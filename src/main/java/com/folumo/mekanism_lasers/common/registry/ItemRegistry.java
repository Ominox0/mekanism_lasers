package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.item.RemoteControlItem;
import mekanism.common.block.BlockBounding;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Mekanism_lasers.MOD_ID);
    public static final ItemRegistryObject<RemoteControlItem> REMOTE_CONTROL = ITEMS.registerItem("remote_control", RemoteControlItem::new);

    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mekanism_lasers.MOD_ID);

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MEKANISM_LASERS_ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(() -> BlockRegistry.ULTIMATE_LASER.getItemStack().getItem().getDefaultInstance())
                .title(Component.translatable("item_group." + Mekanism_lasers.MOD_ID))
                .displayItems((displayParameters, output) -> {
                    ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));

                    for (Holder<Block> blockHolder : BlockRegistry.BLOCKS.getPrimaryEntries()) {
                        Block block = blockHolder.value();
                        if (!(block instanceof BlockBounding)) {
                            output.accept(new ItemStack(block));
                        }
                    }

                })
                .build());
    }
}
