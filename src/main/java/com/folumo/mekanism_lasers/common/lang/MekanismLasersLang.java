package com.folumo.mekanism_lasers.common.lang;

import mekanism.api.text.ILangEntry;
import mekanism.common.Mekanism;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum MekanismLasersLang implements ILangEntry {


    DESCRIPTION_LASER_STOPPER("description", "laser_stopper"),
    DESCRIPTION_LASER("description", "laser"),
    DESCRIPTION_LASER_TOGGLEABLE("description", "laser_toggleable"),
    DESCRIPTION_ORE_GENERATOR("description", "ore_generator"),
    DESCRIPTION_ENERGY_STORAGE_CASING("description", "energy_storage_casing"),
    DESCRIPTION_ENERGY_STORAGE_PORT("description", "energy_storage_port"),
    DESCRIPTION_ENERGY_STORAGE_CELL("description", "energy_storage_cell"),
    ENERGY_STORAGE_PORT_MODE("es", "port.mode")
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
