package com.folumo.mekanism_lasers;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.LongValue basicLaserEnergyCap;
    public static final ModConfigSpec.LongValue basicLaserEnergyUse;

    public static final ModConfigSpec.LongValue advancedLaserEnergyCap;
    public static final ModConfigSpec.LongValue advancedLaserEnergyUse;

    public static final ModConfigSpec.LongValue eliteLaserEnergyCap;
    public static final ModConfigSpec.LongValue eliteLaserEnergyUse;

    public static final ModConfigSpec.LongValue ultimateLaserEnergyCap;
    public static final ModConfigSpec.LongValue ultimateLaserEnergyUse;

    public static final ModConfigSpec.IntValue ODECM_Progress;
    public static final ModConfigSpec.IntValue ODECM_ProgressPerTick;
    public static final ModConfigSpec.LongValue ODECM_TickCost;
    public static final ModConfigSpec.IntValue ODECM_OutputAmount;

    public static final ModConfigSpec.LongValue ODECM_LiquidGasIO;

    public static final ModConfigSpec.BooleanValue ODECM_CanExplode;
    public static final ModConfigSpec.LongValue ODECM_Explosion;

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> ODECM_Generator;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ODECM_ImplodeBlacklist;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ODECM_ImplodeWhitelist;

    public static final ModConfigSpec.IntValue oreGeneratorCooldown;
    public static final ModConfigSpec.LongValue oreGeneratorFabricationCost;
    public static final ModConfigSpec.IntValue oreGeneratorNumberOfOres;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> blacklistedOres;


    static  {
        BUILDER.push("Mekanism Lasers Config");

        basicLaserEnergyCap = BUILDER.comment("Energy config for basic laser").defineInRange("basicLaserEnergyCap", 8000000L, 0, Long.MAX_VALUE);
        basicLaserEnergyUse = BUILDER.defineInRange("basicLaserEnergyUse", 30000L, 0, Long.MAX_VALUE);

        advancedLaserEnergyCap = BUILDER.comment("Energy config for advanced laser").defineInRange("advancedLaserEnergyCap", 32000000L, 0, Long.MAX_VALUE);
        advancedLaserEnergyUse = BUILDER.defineInRange("advancedLaserEnergyUse", 120000L, 0, Long.MAX_VALUE);

        eliteLaserEnergyCap = BUILDER.comment("Energy config for elite laser").defineInRange("eliteLaserEnergyCap", 128000000L, 0, Long.MAX_VALUE);
        eliteLaserEnergyUse = BUILDER.defineInRange("eliteLaserEnergyUse", 480000L, 0, Long.MAX_VALUE);

        ultimateLaserEnergyCap = BUILDER.comment("Energy config for ultimate laser").defineInRange("ultimateLaserEnergyCap", 512000000L, 0, Long.MAX_VALUE);
        ultimateLaserEnergyUse = BUILDER.defineInRange("ultimateLaserEnergyUse", 1920000L, 0, Long.MAX_VALUE);

        ODECM_Progress = BUILDER.comment("Ticks required for ODECM cycle").defineInRange("ODECM_Progress", 200, 0, Integer.MAX_VALUE);
        ODECM_ProgressPerTick = BUILDER.comment("Progress per tick").defineInRange("ODECM_ProgressPerTick", 1, 0, Integer.MAX_VALUE);
        ODECM_TickCost = BUILDER.comment("Energy cost of one tick for ODECM").defineInRange("ODECM_TickCost", 100_000L, 0, Long.MAX_VALUE);
        ODECM_OutputAmount = BUILDER.comment("How many ores should ODECM return after a cycle").defineInRange("ODECM_OutputAmount", 1, 0, Integer.MAX_VALUE);

        ODECM_LiquidGasIO = BUILDER.comment("This determens how fast the ODECM consumes water, and outputs steam, using the formula (ODECM_TickCost * ODECM_Generator (version)) / X").defineInRange("ODECM_LiquidGasIO", 20, 0, Long.MAX_VALUE);

        ODECM_CanExplode = BUILDER.comment("Can the ODECM implode (suck in surrounding blocks and blast them back out) when it is starved of water/steam?").define("ODECM_CanExplode", true);
        ODECM_Explosion = BUILDER.comment("Radius (in blocks) of the ODECM implosion").defineInRange("ODECM_Explosion", 12, 1, Long.MAX_VALUE);

        oreGeneratorCooldown = BUILDER.comment("[Depreciated] Cooldown of the ore generator (in seconds)").defineInRange("oreGeneratorCooldown", 4, 0, Integer.MAX_VALUE);
        oreGeneratorFabricationCost = BUILDER.comment("[Depreciated] Energy cost of fabricating ore/ores").defineInRange("oreGeneratorFabricationCost", 16_000_000L, 0, Long.MAX_VALUE);
        oreGeneratorNumberOfOres = BUILDER.comment("[Depreciated] How much ores do you get from one cycle").defineInRange("oreGeneratorNumberOfOres", 1, 1, Integer.MAX_VALUE);

        blacklistedOres = BUILDER.comment("List of ores to blacklist from generation")
                .defineListAllowEmpty("blacklistedOres", List.of(
                        "justdirethings:raw_ferricore_ore",
                        "justdirethings:raw_blazegold_ore",
                        "justdirethings:raw_celestigem_ore",
                        "justdirethings:raw_eclipsealloy_ore",
                        "justdirethings:raw_coal_t1_ore",
                        "justdirethings:raw_coal_t2_ore",
                        "justdirethings:raw_coal_t3_ore",
                        "justdirethings:raw_coal_t4_ore"
                ), () -> "", o -> o instanceof String);

        ODECM_Generator = BUILDER.comment("List should have amount of elements >= the amount of ODECM generator versions, each number tells the generator by how much will it improve")
                .defineList("ODECM_Generator", List.of(1, 2, 3, 6, 9, 25), () -> 0, o -> o instanceof Integer);

        ODECM_ImplodeBlacklist = BUILDER.comment("Block IDs the ODECM implosion will NEVER pull in. Takes priority over the whitelist.")
                .defineListAllowEmpty("ODECM_ImplodeBlacklist", List.of(), () -> "", o -> o instanceof String);

        ODECM_ImplodeWhitelist = BUILDER.comment("Blocks with a block entity (tile entities) are excluded from the implosion by default; add block IDs here to allow those specific ones to be pulled in.")
                .defineListAllowEmpty("ODECM_ImplodeWhitelist", List.of("mekanism:structural_glass"), () -> "", o -> o instanceof String);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }


}