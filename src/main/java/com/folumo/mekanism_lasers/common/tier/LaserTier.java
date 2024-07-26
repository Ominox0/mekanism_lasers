package com.folumo.mekanism_lasers.common.tier;


import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;


public enum LaserTier implements ITier {
    BASIC(BaseTier.BASIC, 4000000L, 15000L),
    ADVANCED(BaseTier.ADVANCED, 8000000L, 20000L),
    ELITE(BaseTier.ELITE, 16000000L, 25000L),
    ULTIMATE(BaseTier.ULTIMATE, 32000000L, 45000L),
    CREATIVE(BaseTier.CREATIVE, Long.MAX_VALUE, Long.MAX_VALUE)
    ;

    private final BaseTier baseTier;
    private final Long energyCap;
    private final Long energyUsage;

    LaserTier(BaseTier baseTier, Long energyCap, Long energyUsage) {
        this.baseTier = baseTier;
        this.energyCap = energyCap;
        this.energyUsage = energyUsage;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public Long getEnergyCap(){
        return energyCap;
    }

    public Long getEnergyUsage(){
        return energyUsage;
    }

}
