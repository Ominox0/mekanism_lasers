package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.tier.LaserTier;
import mekanism.common.block.attribute.Attribute;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ToggleableLaserBlockEntity extends LaserBlockEntity {
    private boolean active = true;
    private final LaserTier tier;

    public ToggleableLaserBlockEntity(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        tier = Attribute.getTier(blockProvider, LaserTier.class);
    }

    public boolean setLaserActivity(boolean isActive, Player player){
        if (ownerMatches(player)){
            this.active = isActive;
            return true;
        }
        return false;
    }

    public boolean getLaserActivity(){
        return this.active;
    }

    @Override
    protected long toFire(){
        if (!this.active){
            this.setActive(false);
            return 0L;
        }else {
            return tier.getEnergyUsage();

        }
    }
    @Override
    public @NotNull Component getName() {
        return Component.literal("Toggleable Laser");
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.active = nbt.getBoolean("active");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbtTags, provider);
        nbtTags.putBoolean("active", this.active);
    }
}
