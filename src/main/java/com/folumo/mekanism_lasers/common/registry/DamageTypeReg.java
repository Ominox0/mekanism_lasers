package com.folumo.mekanism_lasers.common.registry;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import mekanism.common.registration.impl.DamageTypeRegister;
import mekanism.common.registration.impl.MekanismDamageType;

public class DamageTypeReg {
    public static final DamageTypeRegister DAMAGE_TYPES = new DamageTypeRegister(Mekanism_lasers.MOD_ID);

    public static final MekanismDamageType ODECM = DAMAGE_TYPES.create("odecm");
}