package com.folumo.mekanism_lasers;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue oreGeneratorCooldown;
    public static final ModConfigSpec.LongValue oreGeneratorFabricationCost;
    public static final ModConfigSpec.IntValue oreGeneratorNumberOfOres;


    static  {
        BUILDER.push("Mekanism Lasers Config");

        oreGeneratorCooldown = BUILDER.comment("Cooldown of the ore generator (in seconds)").defineInRange("oreGeneratorCooldown", 4, 0, Integer.MAX_VALUE);
        oreGeneratorFabricationCost = BUILDER.comment("Energy cost of fabricating ore/ores").defineInRange("oreGeneratorFabricationCost", 16_000_000L, 0, Integer.MAX_VALUE);
        oreGeneratorNumberOfOres = BUILDER.comment("How much ores do you get from one cycle").defineInRange("oreGeneratorNumberOfOres", 1, 1, Integer.MAX_VALUE);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }


}
