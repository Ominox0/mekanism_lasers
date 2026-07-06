# Reference — Registries

All deferred registers. These use Mekanism's registration helpers (`BlockDeferredRegister`,
`TileEntityTypeDeferredRegister`, `ItemDeferredRegister`, `ContainerTypeDeferredRegister`) plus
vanilla `DeferredRegister` for creative tabs, data components, and damage types.

---

## `BlockRegistry`

`common.registry.BlockRegistry` — registers all blocks and their block-items into `BLOCKS`.

**`BLOCKS`** — `BlockDeferredRegister` for the mod.

**Registered blocks** (each is a `BlockRegistryObject`):
- `ENERGIZED_FENCE` — plain `Block` with a fence-post collision shape (`BlockShapes.box(4,0,4,12,16,12)`); ODECM structural block.
- `LASER_STOPPER` → [`LaserStopper`](blocks.md#laserstopper).
- `LASER_SPLITTER` → [`LaserSplitter`](blocks.md#lasersplitter).
- `ENERGY_TRANSFORMER` → [`EnergyTransformer`](blocks.md#energytransformer).
- `BASIC/ADVANCED/ELITE/ULTIMATE/CREATIVE_LASER` → [`Laser`](blocks.md#laser) with a `LaserBlockItem`, via `registerLaser`.
- `BASIC/…/CREATIVE_TOGGLEABLE_LASER` → [`ToggleableLaser`](blocks.md#toggleablelaser) with a `ToggleableLaserBlockItem`, via `registerToggleableLaser`.
- `ORE_GENERATOR` → [`OreGenerator`](blocks.md#oregenerator).
- `INTERFACE_BLOCK` → [`InterfaceBlock`](blocks.md#interfaceblock).
- `ODECM` → [`ODECM_Block`](blocks.md#odecm_block).
- `ENERGY_STORAGE_CASING` / `ENERGY_STORAGE_PORT` / `ENERGY_STORAGE_CELL` — Mekanism `BlockTile` wrappers around the energy-storage tiles.

**Helpers**
- `registerLaser(type)` / `registerToggleableLaser(type)` — register a tiered laser block + item under
  `<tier>_laser` / `<tier>_toggleable_laser`.
- `registerTieredBlock(type/tier, suffix, blockSupplier, itemCreator)` — names a block
  `<baseTier lower name> + suffix` and registers it with a custom block-item factory.

---

## `BlockTypeRegistry`

`common.registry.BlockTypeRegistry` — builds the Mekanism `BlockTypeTile` descriptor for each block
(shape, attributes, energy config, GUI, sound). These descriptors are passed to the `Block`
constructors in `BlockRegistry`.

Key descriptors:
- `LASER_STOPPER` / `ODECM` — custom shape, no facing/redstone/particle attributes.
- `LASER_SPLITTER` — energy config (`getMaxEnergy()`), `ACTIVE` + selectable `FACING`, laser-receptor + configurable capabilities added at the tile-type level.
- `ENERGY_TRANSFORMER` — fixed 8 GFE energy config, `ACTIVE` + `FACING`.
- `BASIC..CREATIVE_LASER` (`createLaser`) and `..._TOGGLEABLE_LASER` (`createToggleableLaser`) — tier attribute, selectable `FACING`, security, per-tier energy config, laser shape, laser sound.
- `ORE_GENERATOR` — energy config, sound, GUI (`ContainerTypeRegistry.ORE_GENERATOR`), `ACTIVE`+`FACING`+security, custom shape.
- `INTERFACE_BLOCK` — GUI (`ContainerTypeRegistry.INTERFACE_BLOCK`), `ACTIVE`+`FACING`+security.
- `ENERGY_STORAGE_CASING` — inventory + comparator, external multiblock.
- `ENERGY_STORAGE_PORT` — inventory + comparator + `ACTIVE`, external multiblock.
- `ENERGY_STORAGE_CELL` — custom shape, energy config, internal multiblock.

---

## `BlockEntityTypeRegistry`

`common.registry.BlockEntityTypeRegistry` — registers tile-entity types via `mekBuilder`, wiring
tickers and capabilities.

**`BLOCK_ENTITY_TYPES`** — `TileEntityTypeDeferredRegister`.

Each entry binds a block to its tile constructor and adds a server (and sometimes client) ticker and
capabilities:
- `LASER_STOPPER`, `ODECM`, `LASER_SPLITTER`, `ENERGY_TRANSFORMER` — server ticker; laser-receptor
  and/or configurable capabilities as appropriate.
- `BASIC..CREATIVE_LASER` (`createLaser`) and `..._TOGGLEABLE_LASER` (`createToggleableLaser`) — server
  ticker + laser-receptor capability.
- `ORE_GENERATOR` — server ticker + laser-receptor.
- `INTERFACE_BLOCK` — server ticker.
- `ENERGY_STORAGE_CASING` — client + server ticker, configurable, item capability removed.
- `ENERGY_STORAGE_PORT` — client + server ticker, laser-receptor + configurable.
- `ENERGY_STORAGE_CELL` — plain `builder` (no ticker/caps).

Mekanism's `mekBuilder` automatically exposes the energy capability for tiles that have energy
containers, which is why the ports/lasers connect to cables without an explicit energy capability line.

---

## `ContainerTypeRegistry`

`common.registry.ContainerTypeRegistry` — GUI menu types (`CONTAINER_TYPES`).
- `ORE_GENERATOR` — `MekanismTileContainer<OreGeneratorBlockEntity>` (custom, tied to the tile class).
- `INTERFACE_BLOCK` — `MekanismTileContainer<InterfaceBlockEntity>`.
- `LASER_CONFIGURATOR` — item-based container (`LaserConfiguratorContainer`) bound to the
  `LaserConfiguratorItem`.

---

## `ItemRegistry`

`common.registry.ItemRegistry` — items (`ITEMS`) and the creative tab.
- `REMOTE_CONTROL` → [`RemoteControlItem`](items.md#remotecontrolitem).
- `LASER_CONFIGURATOR` → [`LaserConfiguratorItem`](items.md#laserconfiguratoritem).
- Inner class **`ModItemTab`** — registers the `CREATIVE_MODE_TABS` register and the mod's creative
  tab (`main`), whose icon is the ultimate laser and whose contents are every registered item plus a
  block-item for every non-`BlockBounding` block.

---

## `ComponentRegistry`

`common.registry.ComponentRegistry` — custom data-component types used by the Remote Control.
- `RC_BLOCKPOS` — `DataComponentType<List<BlockPos>>`, persistent, cached encoding; the linked laser positions.
- `RC_ACTIVITY` — `DataComponentType<Boolean>`, persistent; the remote's shared on/off flag.
- `register(name, builderOp)` — helper that builds and registers a data component type into the vanilla registry.

---

## `DamageTypeReg`

`common.registry.DamageTypeReg` — Mekanism damage-type register (`DAMAGE_TYPES`).
- `ODECM` — a `MekanismDamageType` named `odecm`. (Present for the ODECM malfunction; the current
  implosion does not apply it directly.)
