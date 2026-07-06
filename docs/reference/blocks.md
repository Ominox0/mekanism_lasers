# Reference — Blocks

Block classes in `common.block`. Most extend Mekanism's `BlockTile.BlockTileModel<TILE, TYPE>`, which
provides the tile-bound, model-rendered block behaviour; the interesting ones override interaction or
shape.

---

## `Laser`

`BlockTileModel<LaserBlockEntity, …>`. Plain tiered laser block; only sets a steel map colour. All
behaviour is in the tile and the block-type descriptor.

## `ToggleableLaser`

`BlockTileModel<ToggleableLaserBlockEntity, …>`.
- `useWithoutItem(state, world, pos, player, hit)` — server-side, toggles the tile's laser activity
  (`getLaserActivity()` → `setLaserActivity(!…, player)`) and returns `SUCCESS`. Ownership is enforced
  inside `setLaserActivity`.

## `EnergyTransformer`

`BlockTileModel<EnergyTransformerBlockEntity, …>`. Plain; steel map colour.

## `InterfaceBlock`

`BlockTileModel<InterfaceBlockEntity, …>`. Plain; steel map colour. GUI/behaviour come from the tile
and block-type.

## `OreGenerator`

`BlockTileModel<OreGeneratorBlockEntity, …>`. Plain; steel map colour.

## `LaserSplitter`

`BlockTileModel<LaserSplitterBlockEntity, …>`. Plain; steel map colour. Splitting logic and the
configurator interaction live in the tile.

## `LaserStopper`

`BlockTileModel<LaserStopperBlockEntity, …>` — a disguising block.
- `useItemOn(stack, …)` — if the used item is a `BlockItem`, calls `changeTexture` on the tile with
  that block's default state (the disguise), returns `SUCCESS`.
- `getShape(state, world, pos, context)` — returns the disguise block's collision shape when set,
  otherwise the default.
- `getRenderShape(state)` — `INVISIBLE`; the block itself is drawn by
  [`LaserStopperRenderer`](client.md#laserstopperrenderer) so the disguise can be shown.

## `ODECM_Block`

`BlockTileModel<ODECM_BlockEntity, …>` — the ODECM controller block.
- **`ORES_TAG`** — `c:ores` block tag; only blocks in this tag can be selected as the target ore.
- **`pattern`** — an unused `List<String>` field (legacy).
- `useItemOn(...)` — server-side. If the held item is a `BlockItem` whose block is in `ORES_TAG` and
  not in [`blacklistedOres`](../configuration.md#ore-selection-shared), sets it as the tile's target
  ore (`setItem`); otherwise messages the player it is blacklisted.
- `useWithoutItem(...)` — server-side; prints the current progress vs. the version-scaled cycle length
  and the selected ore, or a "Can't build" message with the first failing position/expected block.

## `BlockShapes`

`common.block.BlockShapes` — static `VoxelShape` definitions.
- `box(minX..maxZ)` — thin wrapper over `Block.box`.
- Per-direction shape arrays `LASER_STOPPER_SHAPE`, `ORE_GENERATOR_SHAPE`, `ENERGY_STORAGE_CELL_SHAPE`,
  `ENERGIZED_FENCE` (one `VoxelShape` per facing).
- Static initializer builds each shape by combining boxes with Mekanism's `VoxelShapeUtils` and
  rotating it into all six directions.

> Note: there is a **second** `BlockShapes` class in `common.registry`'s import path used by
> `BlockTypeRegistry` (`mekanism.common.content.blocktype.BlockShapes`) — that one is Mekanism's, not
> this mod's. This mod's `BlockShapes` is the voxel-shape holder above.
