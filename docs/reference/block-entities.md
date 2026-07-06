# Reference — Block Entities

Tile classes in `common.block_entity`. Most extend Mekanism's `TileEntityMekanism`,
`TileEntityLaserReceptor` (a laser receptor that can also fire), or the multiblock tile prefabs.

---

## `LaserBlockEntity`

`extends TileEntityLaserReceptor`. A tiered laser emitter with an inventory.

**Fields**: `tier` (`LaserTier`, from the block attribute).

**Constructor** — resolves the tier; if `CREATIVE`, immediately fills the energy buffer to `Long.MAX_VALUE`.

**Methods**
- `getInitialInventory(listener)` — builds a 5×3 grid of output slots (visual/laser-catch storage).
- `receiveLaserEnergy(energy)` — **no-op** (lasers do not accept incoming laser energy into the buffer).
- `addInitialEnergyContainers(builder, listener)` — one back-facing `LaserEnergyContainer` (not externally extractable, always insertable).
- `toFire()` — the tier's per-tick energy-use value (how much beam it fires).
- `getName()` — "Laser".
- `onUpdateServer()` — runs the base laser tick; if `CREATIVE`, tops the buffer back up to max each tick.

## `ToggleableLaserBlockEntity`

`extends LaserBlockEntity`. Adds an on/off switch.

**Fields**: `active` (default `true`), `tier`.

**Methods**
- `setLaserActivity(isActive, player)` — sets `active` if the player owns the block; returns whether it applied.
- `getLaserActivity()` — current `active` state.
- `toFire()` — `0` when inactive (and marks the block inactive), otherwise the tier's energy-use.
- `getName()` — "Toggleable Laser".
- `loadAdditional` / `saveAdditional` — persist `active`.

## `LaserStopperBlockEntity`

`extends TileEntityMekanism implements ILaserReceptor`. A laser-absorbing block that can disguise as another block.

**Fields**: `currentBlockState` (nullable; the disguise).

**Methods**
- `getName()` — "Laser Stopper".
- `receiveLaserEnergy(energy)` — no-op (absorbs, i.e. stops the beam without digging).
- `canLasersDig()` — `false` (beams stop here rather than mining).
- `changeTexture(state)` — set the disguise state and push a block update so clients re-render.
- `getCurrentBlockState()` — the disguise state.
- `loadAdditional` / `saveAdditional` — persist the disguise via `BlockState.CODEC`.

## `EnergyTransformerBlockEntity`

`extends TileEntityLaserReceptor`. A laser receptor that just stores energy.

**Methods**
- `addInitialEnergyContainers(...)` — one `LaserEnergyContainer` that is both insertable and extractable (adapter between the laser network and normal energy transfer).
- `getName()` — "Energy Transformer".
- `onUpdateServer()` — returns `false` (no per-tick logic, no firing).

## `LaserSplitterBlockEntity`

`extends TileEntityLaserReceptor implements IConfigurable`. Receives energy and re-emits it as
perpendicular beams. See [features → Laser Splitter](../features.md#laser-splitter).

**Constants**: `MODE_COUNT = 4`, `mode` (0=corner … 3=4-way, default 1).
Per-direction digging state maps `digging` / `diggingProgress`.

**Core methods**
- `addInitialEnergyContainers(...)` — one back-facing `LaserEnergyContainer` (internal-only insert).
- `getName()` — "Laser Splitter"; `getMaxEnergy()` — the buffer size (10 GFE).
- `toFire()` — returns `0` so the base single-beam logic never fires; splitting is done manually.
- `onUpdateServer()` — calls the base tick (harmless with `toFire()==0`), then if there is stored
  energy and active outputs, divides the energy evenly and fires one beam per output direction,
  extracting the total.
- `outputDirections()` — the output directions for the current mode: always perpendicular to the
  facing axis (a "horizontal-ish" opposite pair first) so a beam never fires back at the source.
- `fireBeam(direction, energy)` — a faithful laser beam: raycasts to the first block; damages entities
  along the path (Mekanism laser damage, subtracting energy); renders the beam particles; then either
  deposits remaining energy into a laser receptor it hit, or digs the block.
- `digBlock(...)` — per-direction digging progress; on completion, breaks the block with a fake player
  (respecting `aestheticWorldDamage`).
- `resetDigging(dir)`, `getEnergyScale(energy)`, `getLaserBox(...)`, `sendLaserDataToPlayers(...)` — helpers mirroring Mekanism's `TileEntityBasicLaser` internals.

**Configurator**
- `onSneakRightClick(player)` — cycles `mode` (mod `MODE_COUNT`), messages the new mode.
- `onRightClick(player)` — messages the current mode.
- `modeName()` — human-readable mode label.

**Persistence**: `loadAdditional` / `saveAdditional` persist `mode`.

## `ODECM_BlockEntity`

`extends TileEntityMekanism implements IConfigurable`. The ODECM controller. See
[features → ODECM](../features.md#odecm) and [multiblock.md](multiblock.md#oregeneratorbase).

**State fields**: `typeV` (current `OreGeneratorBase` version), `item` (target ore block), `progress` /
`maxProgress` (`= ODECM_Progress`), cached IO positions `chestOut`/`powerIn`/`fluidIn`/`gasOut`,
`structureCooldown` + `cachedStructureValid` (structure-check cache), `doExplode` flag, and the
implosion state (`implodeTimer`, `implodeCooldown`, `flyingBlockIds`, and the `IMPLODE_*` constants).

**Version / target**
- `nextVe()` — advance to the next generator version (wraps to v1).
- `getVType()` — current version (defaults to v1).
- `setItem(block)` / `getItem()` / `hasItem()` — the target ore block (as a `Block` / rendered `BlockState`).

**Persistence**: `saveAdditional` / `loadAdditional` persist the version id, target ore id, and progress.

**Structure & resources**
- `isStillBuilt()` — re-validates the structure every ~20 ticks (cached), updating the IO positions.
- `getEnergyCost(block)` — base per-tick energy cost (currently `ODECM_TickCost`).
- `getGeneratorMultiplier()` — the current version's speed/cost multiplier from `ODECM_Generator`,
  guarded against short lists and non-positive values.
- `simulateAndReduce(energy, cost)` — extract-if-affordable helper for an energy cube / energy transformer.
- `tickPower(ability, cost)` / `tickFluid(ability, cost, energyResult)` / `tickGas(ability, cost, energyResult)`
  — consume power / water / output steam per the version's ability; the fluid/gas methods set
  `doExplode` if they fail while power succeeded.

**Generation**
- `tickGenerated()` — the main per-tick logic: compute multiplier and costs, run the power/fluid/gas
  steps, trigger the implosion if starved, then (if all requirements met) add progress and, on
  reaching the version-scaled threshold, produce output.
- `generateOutput()` — inserts `ODECM_OutputAmount` of the target ore into the structure's Personal
  Barrel, respecting slot limits and spilling across slots.

**Implosion** (replaces the old explosion)
- `startImplosion()` — collects eligible blocks in `ODECM_Explosion` radius (skipping air/fluid/
  unbreakable, blacklisted, and block-entity blocks unless whitelisted), caps at `IMPLODE_MAX_BLOCKS`,
  converts them to `FallingBlockEntity` projectiles pulled toward the core, and starts the timer.
- `tickImplosion()` — each tick steers the projectiles toward the core; on the final tick, destroys a
  `IMPLODE_DESTROY_CHANCE` fraction and launches the rest outward.
- `endImplosion()` — clears state and starts the cooldown.

**Tick / interaction**
- `onUpdateServer()` — drives the implosion if active; otherwise, if built and targeting an ore, runs
  `tickGenerated()`; else resets progress. Decrements the implosion cooldown.
- `onSneakRightClick(player)` — cycles the generator version and messages it.
- `onRightClick(player)` — creative-only: force-builds the current version's structure around the core
  and marks changed.

## `OreGeneratorBlockEntity`

`extends TileEntityLaserReceptor`. The legacy standalone ore generator.

**Fields/statics**: `coolDown`; static `energyCap` (64 MFE), `usage`/`cooldown`/`numberOfOres` from
config; `energyContainer`.

**Methods**
- `getInitialInventory(listener)` — 5×3 output grid.
- `getUsage()` / `getEnergyCap()` — energy statics for the block-type energy config.
- `receiveLaserEnergy(energy)` — insert incoming laser energy into the buffer.
- `getName()` — "Ore Generator".
- `addInitialEnergyContainers(...)` — one internal-only `LaserEnergyContainer`.
- `getEnergyContainer()` — the buffer (used by the GUI energy gauge).
- `onUpdateServer()` — on a `cooldown`-second timer, if enough energy is stored, calls `generateOres()`.
- `generateOres()` — inserts up to `numberOfOres` random ores into the inventory, spending
  `usage/numberOfOres` energy each until the inventory is full.
- `getRandomDrop()` (static) — picks a random ore from the `c:ores`-equivalent tag, excluding
  `blacklistedOres` and the `mekanism_lasers:ore_blacklist` tag.

## `InterfaceBlockEntity`

`extends TileEntityMekanism`. Turns a redstone edge into a Remote Control activation.

**Fields**: static `lastRedstoneLevel` (edge tracking).

**Methods**
- `getName()` — "Interface Block".
- `getInitialInventory(listener)` — a single input slot for the Remote Control.
- `hasNeighborSignal(level, pos)` — true if any of the six neighbours emits redstone.
- `onUpdateServer()` — on a rising redstone edge, if the slot holds a `RemoteControlItem`, resolves the
  block's owner and invokes `switchLaserMode` on their behalf (toggling all lasers linked to that
  remote).

## Energy Storage block entities

- **`EnergyStorageCasingBlockEntity`** — `extends TileEntityMultiblock<energyStorageMultiblockData>`; the
  structure frame. `createMultiblock()` builds the multiblock data; `getManager()` returns
  `Mekanism_lasers.energyStorage`.
- **`EnergyStoragePortBlockEntity`** — `extends EnergyStorageCasingBlockEntity implements ILaserReceptor`;
  the I/O block.
  - `getInitialEnergyContainers(...)` — a proxied holder that exposes the multiblock's energy container,
    allowing **insert when in input mode** (`!getActive()`) and **extract when in output mode**
    (`getActive()`).
  - `persists(type)` — does not persist the energy container on the port itself (it belongs to the structure).
  - `addEnergyTargetCapability(targets, side)` — caches the neighbour energy capability on `side` as an
    output target (used by the multiblock's ejector).
  - `onSneakRightClick(player)` — toggles input/output mode and messages it.
  - `getRedstoneLevel()` — the structure's comparator level.
  - `receiveLaserEnergy(energy)` — feeds laser energy into the formed structure.
  - `canLasersDig()` — `false`.
- **`EnergyStorageCellBlockEntity`** — `extends TileEntityInternalMultiblock`; interior storage.
  - `getInitialEnergyContainers(...)` — an internal `MachineEnergyContainer`.
  - `getEnergyContainer()` — that container (added to the structure on formation).
  - `getMaxEnergy()` (static) — per-cell capacity (16 GFE), used for the block-type energy config.
