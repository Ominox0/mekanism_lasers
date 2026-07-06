# Reference — Core

Entrypoint, configuration, and the laser tier enum.

---

## `Mekanism_lasers`

`com.folumo.mekanism_lasers.Mekanism_lasers` — the `@Mod` entrypoint.

**Constants**
- `MOD_ID = "mekanism_lasers"` — the mod id used everywhere.
- `LOGGER` — SLF4J logger.
- `energyStorage` — `MultiblockManager<energyStorageMultiblockData>` that manages the Energy Storage
  multiblock (cache + validator supplier). Referenced by the casing/port tiles' `getManager()`.
- `ORE_BLACKLIST` — `TagKey<Block>` (`mekanism_lasers:ore_blacklist`); ores in this tag are excluded
  from the legacy Ore Generator's random selection.

**Constructor** `Mekanism_lasers(IEventBus modEventBus, ModContainer modContainer)`
Registers, on the mod event bus, in order: `BlockRegistry.BLOCKS`,
`BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES`, `ContainerTypeRegistry.CONTAINER_TYPES`,
`ItemRegistry.ITEMS`, the creative-tab register, and the common config spec (`Config.SPEC`).

---

## `Config`

`com.folumo.mekanism_lasers.Config` — declares the mod's common config via NeoForge `ModConfigSpec`.

**Fields** — each is a typed `ModConfigSpec.*Value`/`ConfigValue` holding one option. Grouped:
- Laser energy: `basicLaserEnergyCap`/`Use`, `advancedLaserEnergyCap`/`Use`, `eliteLaserEnergyCap`/`Use`,
  `ultimateLaserEnergyCap`/`Use`.
- ODECM: `ODECM_Progress`, `ODECM_ProgressPerTick`, `ODECM_TickCost`, `ODECM_OutputAmount`,
  `ODECM_LiquidGasIO`, `ODECM_CanExplode`, `ODECM_Explosion`, `ODECM_Generator`,
  `ODECM_ImplodeBlacklist`, `ODECM_ImplodeWhitelist`.
- Legacy Ore Generator: `oreGeneratorCooldown`, `oreGeneratorFabricationCost`,
  `oreGeneratorNumberOfOres`.
- Shared: `blacklistedOres`.
- `BUILDER` / `SPEC` — the `ModConfigSpec.Builder` and the built `ModConfigSpec`.

**Static initializer** pushes the `"Mekanism Lasers Config"` section, defines every option (comment,
default, range/validator), then pops and builds `SPEC`.

See [configuration.md](../configuration.md) for the meaning, defaults, and ranges of every option.

---

## `LaserTier` (enum)

`com.folumo.mekanism_lasers.common.tier.LaserTier` — implements Mekanism's `ITier`.

**Values** `BASIC, ADVANCED, ELITE, ULTIMATE, CREATIVE`. Each binds a Mekanism `BaseTier` (for colour
and naming) and two `LongSupplier`s for energy cap and energy use, read from `Config`. `CREATIVE`
uses `Long.MAX_VALUE` for both (not configurable).

**Methods**
- `getBaseTier()` → the underlying Mekanism `BaseTier`.
- `getEnergyCap()` → `LongSupplier` for the tier's energy buffer.
- `getEnergyUsage()` → `LongSupplier` for the tier's per-tick fire amount.

Used by [`BlockTypeRegistry`](registries.md#blocktyperegistry) (energy config + tier attribute),
[`LaserBlockEntity`](block-entities.md#laserblockentity)/`ToggleableLaserBlockEntity` (fire amount and
creative top-up), and the tiered block-items (name colour).
