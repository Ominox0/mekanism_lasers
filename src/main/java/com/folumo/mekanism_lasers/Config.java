package com.folumo.mekanism_lasers;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue oreGeneratorCooldown;
    public static final ModConfigSpec.LongValue oreGeneratorFabricationCost;
    public static final ModConfigSpec.IntValue oreGeneratorNumberOfOres;
    public static final ModConfigSpec.ConfigValue<List<String>> blacklistedOres;


    static  {
        BUILDER.push("Mekanism Lasers Config");

        oreGeneratorCooldown = BUILDER.comment("Cooldown of the ore generator (in seconds)").defineInRange("oreGeneratorCooldown", 4, 0, Integer.MAX_VALUE);
        oreGeneratorFabricationCost = BUILDER.comment("Energy cost of fabricating ore/ores").defineInRange("oreGeneratorFabricationCost", 16_000_000L, 0, Integer.MAX_VALUE);
        oreGeneratorNumberOfOres = BUILDER.comment("How much ores do you get from one cycle").defineInRange("oreGeneratorNumberOfOres", 1, 1, Integer.MAX_VALUE);

        blacklistedOres = BUILDER.comment("List of ores to blacklist from generation")
                .define("blacklistedOres", List.of(
                        "justdirethings:raw_ferricore_ore",
                        "justdirethings:raw_blazegold_ore",
                        "justdirethings:raw_celestigem_ore",
                        "justdirethings:raw_eclipsealloy_ore",
                        "justdirethings:raw_coal_t1_ore",
                        "justdirethings:raw_coal_t2_ore",
                        "justdirethings:raw_coal_t3_ore",
                        "justdirethings:raw_coal_t4_ore"
                ));

        BUILDER.pop();
        SPEC = BUILDER.build();

    }


}
