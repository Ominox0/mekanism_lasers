package com.folumo.mekanism_lasers.common.tier;


import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;


public enum ToggleableLaserTier implements ITier {
    BASIC(BaseTier.BASIC, FloatingLong.createConst(4000000L), FloatingLong.createConst(15000L)),
    ADVANCED(BaseTier.ADVANCED, FloatingLong.createConst(8000000L), FloatingLong.createConst(20000L)),
    ELITE(BaseTier.ELITE, FloatingLong.createConst(16000000L), FloatingLong.createConst(25000L)),
    ULTIMATE(BaseTier.ULTIMATE, FloatingLong.createConst(32000000L), FloatingLong.createConst(45000L))
    ;

    private final BaseTier baseTier;
    private final FloatingLong energyCap;
    private final FloatingLong energyUsage;

    ToggleableLaserTier(BaseTier baseTier, FloatingLong energyCap, FloatingLong energyUsage) {
        this.baseTier = baseTier;
        this.energyCap = energyCap;
        this.energyUsage = energyUsage;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
    }

    public FloatingLong getEnergyCap(){
        return energyCap;
    }

    public FloatingLong getEnergyUsage(){
        return energyUsage;
    }

}
