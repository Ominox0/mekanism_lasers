package com.folumo.mekanism_lasers.common.lang;

import mekanism.api.text.ILangEntry;
import mekanism.common.Mekanism;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum MekanismLasersLang implements ILangEntry {


    DESCRIPTION_LASER_STOPPER("description", "laser_stopper"),
    DESCRIPTION_LASER("description", "laser"),
    DESCRIPTION_LASER_TOGGLEABLE("description", "laser_toggleable"),
    DESCRIPTION_ORE_GENERATOR("description", "ore_generator")
    ;


    private final String key;

    MekanismLasersLang(String type, String path) {
        this(Util.makeDescriptionId(type, Mekanism.rl(path)));
    }

    MekanismLasersLang(String key) {
        this.key = key;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return key;
    }

}
