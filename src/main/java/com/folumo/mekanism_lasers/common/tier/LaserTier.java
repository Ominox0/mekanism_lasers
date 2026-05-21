package com.folumo.mekanism_lasers.common.tier;


import com.folumo.mekanism_lasers.Config;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;

import java.util.function.LongSupplier;


public enum LaserTier implements ITier {
    BASIC(BaseTier.BASIC, Config.basicLaserEnergyCap, Config.basicLaserEnergyUse),
    ADVANCED(BaseTier.ADVANCED, Config.advancedLaserEnergyCap, Config.advancedLaserEnergyUse),
    ELITE(BaseTier.ELITE, Config.eliteLaserEnergyCap, Config.eliteLaserEnergyUse),
    ULTIMATE(BaseTier.ULTIMATE, Config.ultimateLaserEnergyCap, Config.ultimateLaserEnergyUse),
    CREATIVE(BaseTier.CREATIVE, () -> Long.MAX_VALUE, () -> Long.MAX_VALUE)
    ;

    private final BaseTier baseTier;
    private final LongSupplier energyCap;
    private final LongSupplier energyUsage;

    LaserTier(BaseTier baseTier, LongSupplier energyCap, LongSupplier energyUsage) {
        this.baseTier = baseTier;
        this.energyCap = energyCap;
        this.energyUsage = energyUsage;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public LongSupplier getEnergyCap(){
        return energyCap;
    }

    public LongSupplier getEnergyUsage(){
        return energyUsage;
    }

}
