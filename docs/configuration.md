# Configuration reference

All options live in the **common** config file `config/mekanism_lasers-common.toml`, built by
[`Config`](reference/core.md#config). They are declared under the `"Mekanism Lasers Config"` section.

> **Note:** NeoForge writes defaults to the TOML the first time the mod loads and thereafter prefers
> the saved value over the code default. If you change a default in `Config.java`, existing worlds
> keep their old saved value until the key is edited or the file is regenerated.

## Laser energy (per tier)

Each laser tier has an energy **capacity** (buffer) and an energy **use** (fired per tick). The
`CREATIVE` tier is not configurable — it is hard-coded to `Long.MAX_VALUE` in
[`LaserTier`](reference/core.md#lasertier-enum).

| Option | Default | Meaning |
| --- | --- | --- |
| `basicLaserEnergyCap` | 8,000,000 | Basic laser energy buffer |
| `basicLaserEnergyUse` | 30,000 | Basic laser energy fired per tick |
| `advancedLaserEnergyCap` | 32,000,000 | Advanced laser buffer |
| `advancedLaserEnergyUse` | 120,000 | Advanced laser use/tick |
| `eliteLaserEnergyCap` | 128,000,000 | Elite laser buffer |
| `eliteLaserEnergyUse` | 480,000 | Elite laser use/tick |
| `ultimateLaserEnergyCap` | 512,000,000 | Ultimate laser buffer |
| `ultimateLaserEnergyUse` | 1,920,000 | Ultimate laser use/tick |

All are `long`, range `0 .. Long.MAX_VALUE`.

## ODECM

| Option | Default | Range | Meaning |
| --- | --- | --- | --- |
| `ODECM_Progress` | 200 | `> 0` | Ticks of progress required for one generation cycle (the "cycle length"). |
| `ODECM_ProgressPerTick` | 1 | `≥ 0` | Progress added each tick that all resource requirements are met. |
| `ODECM_TickCost` | 100,000 | `≥ 0` | Base energy cost per tick (multiplied by the generator-version multiplier). |
| `ODECM_OutputAmount` | 1 | `≥ 0` | How many ore blocks are produced per completed cycle. |
| `ODECM_LiquidGasIO` | 20 | `≥ 0` | Divisor: water consumed / steam produced per tick = `(tick cost) / this`. `0` disables fluid/gas cost. |
| `ODECM_CanExplode` | true | — | Whether the ODECM triggers its implosion when starved of water/steam while powered. |
| `ODECM_Explosion` | 12 | `≥ 1` | **Radius (blocks)** of the implosion effect (previously the explosion magnitude, default 80). |
| `ODECM_Generator` | `[1, 2, 3, 6, 9, 25]` | list of ints | Per-generator-version multiplier. Index = generator version index. Higher = faster cycle *and* higher energy cost. Should have at least as many entries as there are generator versions. |
| `ODECM_ImplodeBlacklist` | `[]` | list of block IDs | Blocks the implosion will **never** pull in. Takes priority over the whitelist. |
| `ODECM_ImplodeWhitelist` | `["mekanism:structural_glass"]` | list of block IDs | Blocks that have a block entity are excluded from the implosion by default; IDs listed here are allowed anyway. |

See [features.md → ODECM](features.md#odecm) for how these combine, and
[reference/block-entities.md → ODECM_BlockEntity](reference/block-entities.md#odecm_blockentity) for
the exact math.

## Ore Generator (legacy / deprecated)

The standalone Ore Generator predates the ODECM. Its options are marked `[Depreciated]`.

| Option | Default | Range | Meaning |
| --- | --- | --- | --- |
| `oreGeneratorCooldown` | 4 | `> 0` | Seconds between fabrication cycles. |
| `oreGeneratorFabricationCost` | 16,000,000 | `≥ 0` | Energy cost per fabrication cycle. |
| `oreGeneratorNumberOfOres` | 1 | `> 1`* | Ores produced per cycle. (*Range is declared `> 1` in code even though the default is 1.) |

## Ore selection (shared)

| Option | Default | Meaning |
| --- | --- | --- |
| `blacklistedOres` | a list of `justdirethings:*` ores | Ore block IDs excluded from random generation (used by the Ore Generator) and from being selected as an ODECM target (checked in `ODECM_Block`). |

The Ore Generator additionally excludes anything in the `mekanism_lasers:ore_blacklist` block tag
(see [`Mekanism_lasers.ORE_BLACKLIST`](reference/core.md#mekanism_lasers)).
