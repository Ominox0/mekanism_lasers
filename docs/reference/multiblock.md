# Reference — Multiblock

The ODECM structure definitions (`common.multiblock.OreGeneratorBase`) and the Energy Storage
multiblock (`common.multiblock.data` / `.container` / `.validator`). Also covers the
`LaserConfiguratorContainer` and `LaserStorageInventory` helpers.

---

## `OreGeneratorBase`

`common.multiblock.OreGeneratorBase` — a data-driven definition of one ODECM structure "version"
(shape + which block is which role + what resources it needs). Despite the name it is **not** a
Mekanism multiblock; it is a lightweight pattern matcher used by `ODECM_BlockEntity`.

**Fields**: `layers` (the block pattern, bottom-to-top), `name`, `keys` (char → `Block`),
`indexCore` (which layer index is the core's Y level), `index` (version index), `ability`
(`GeneratorAbility`).

**Methods**
- `getName()` / `getAbility()` / `getIndex()`.
- `rotate(x, z, rotation)` — rotate a local offset by 0/90/180/270°.
- `forceBuild(pos, level, player)` — creative-only; places the whole structure around `pos`. **Skips**
  the core's own position (so it never resets the controller tile) and pushes explicit block updates so
  the structure appears immediately for clients.
- `canBuild(pos, level)` — tries all four rotations; returns a `BuildCheckResult` describing success
  (with the located IO positions) or the first mismatch (position/expected/actual).

**Nested types**
- `Layer(size, layers)` — one horizontal slice (array of equal-length rows).
- `GeneratorAbility` — flags `needsPower` / `needsWater` / `outputsItems` / `outputsGas`, with fluent
  `with…`/`without…` setters. Controls which resource steps run in `tickGenerated`.
- `BuildCheckResult` (record) — `success`, mismatch `pos`/`expected`/`actual`, and located IO
  positions `outLocation`/`inPower`/`inFluid`/`outGas`; factory methods `success(...)` / `fail(...)`.

**Structure key characters**: `I` input power, `O` output item (Personal Barrel), `F` fluid input,
`G` gas output, plus decorative/frame blocks.

**Predefined versions** `GENERATOR_V1..V4` and the `MAPPED` (index → version) map:
- **v1** — small; power + item output only.
- **v2** — larger; power + item output.
- **v3** — larger still; power + item output.
- **v4** — full featured; power + water + item output + steam output.

---

## `energyStorageMultiblockData`

`common.multiblock.data.energyStorageMultiblockData` `extends MultiblockData`. The formed Energy
Storage structure's runtime data.

**Fields**: `energyOutputTargets` (cached ejection targets), `energyContainer` (the structure's
`energyStorageMultiblockContainer`, registered as the multiblock's energy container).

**Methods**
- `tick(world)` — ticks the energy container, then, if there are output targets and stored energy,
  emits power to them via Mekanism's `CableUtils.emit`.
- `addEnergy(energy)` — insert energy into the structure (used by the port's laser receiver).
- `addCell(cell)` — register an interior cell with the container.
- `updateEjectors(world)` — rebuild `energyOutputTargets` from the structure's valve (port) positions.

## `energyStorageMultiblockContainer`

`common.multiblock.container.energyStorageMultiblockContainer implements IEnergyContainer`. Aggregates
the interior cells into one logical energy buffer with a per-tick transfer limit.

**Fields**: `cells` (pos → cell energy container), queued/last input & output, `cachedTotal`,
`transferCap`, `storageCap`, `multiblock`.

**Methods**
- `addCell(pos, cell)` — register a cell; adds its capacity to `storageCap` **and** to `transferCap`
  (so the structure can actually move power — without this `transferCap` stays 0 and nothing flows),
  and its energy to `cachedTotal`.
- `getEnergy()` / `setEnergy()` (no-op) / `getMaxEnergy()` — aggregate energy view.
- `getRemainingInput()` / `getRemainingOutput()` — `transferCap` minus what is already queued this tick.
- `insert(amount, action, type)` — queue an input, limited by remaining input rate and free space.
- `extract(amount, action, type)` — queue an output, limited by remaining output rate and stored energy.
- `tick()` — reconcile queued input vs output for the tick, applying the net change across cells;
  reset the queues.
- `removeEnergy(energy)` / `addEnergy(energy)` — drain/fill the cells to match the net change.
- `onContentsChanged()` / `deserializeNBT(...)` — no-ops (state derives from the cells).

## `energyStorageMultiblockValidator`

`common.multiblock.validator.energyStorageMultiblockValidator extends CuboidStructureValidator<…>`.
Validates the fixed **3×4×3** cuboid.

**Fields**: `cells` (collected during validation), `MIN_CUBOID`/`MAX_CUBOID` (both 3×4×3).

**Methods**
- `precheck()` — fetch a 3×4×3 cuboid (all sides except top must be casing/frame).
- `getCasingType(state)` — casing → `FRAME`, port → `VALVE`, else `INVALID`.
- `validateInner(state, chunkMap, pos)` — interior must be air or an Energy Storage Cell; cells are collected.
- `postcheck(structure, chunkMap)` — register every collected cell with the structure.

---

## `LaserConfiguratorContainer`

`common.container.LaserConfiguratorContainer extends MekanismItemContainer`. The (slot-less) item
container backing the Laser Configurator GUI; constructed from the held stack + hand. Registered in
[`ContainerTypeRegistry`](registries.md#containertyperegistry) and shown by
[`LaserConfiguratorItemScreen`](client.md#laserconfiguratoritemscreen).

## `LaserStorageInventory`

`common.container.LaserStorageInventory extends AbstractPersonalStorageItemInventory`. A minimal
personal-storage item inventory (overrides `onContentsChanged` as a no-op). Present as a helper for
laser storage; not central to current behaviour.
