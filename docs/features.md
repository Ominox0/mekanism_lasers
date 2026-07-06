# Feature guide

What every block and item does in-game. For implementation details, follow the links into
[`reference/`](reference/).

---

## Lasers (Basic / Advanced / Elite / Ultimate / Creative)

Directional energy emitters, one per tier. A laser stores energy in a back-facing buffer and, every
tick, fires a beam out of its front face equal to its tier's energy-use value. The beam behaves like
a Mekanism laser: it travels until it hits a block, damages entities in its path, deposits energy
into any **laser receptor** it hits (splitter, energy transformer, ore generator, energy-storage
port), or otherwise slowly digs the block.

- **Tiers** scale buffer size and per-tick output — see [configuration](configuration.md#laser-energy-per-tier).
- **Creative laser** keeps itself topped up to `Long.MAX_VALUE` every tick, so it fires at full power forever.
- Item names are tinted with the tier colour.

Implementation: [`Laser`](reference/blocks.md#laser), [`LaserBlockEntity`](reference/block-entities.md#laserblockentity),
[`LaserTier`](reference/core.md#lasertier-enum).

## Toggleable Lasers (per tier)

Same as the lasers above, but they can be switched **on/off**:

- **Right-click** the block (empty hand) to toggle it (only the owner may toggle).
- When off, it fires nothing (its per-tick fire amount is 0) and marks itself inactive.
- Toggle state is saved to NBT.
- Can also be toggled remotely with the [Remote Control](#remote-control).

Implementation: [`ToggleableLaser`](reference/blocks.md#toggleablelaser),
[`ToggleableLaserBlockEntity`](reference/block-entities.md#toggleablelaserblockentity).

## Remote Control

An item that toggles many linked toggleable lasers at once.

- **Right-click a toggleable laser** (that you own) to link/unlink it. The block position is stored on the item.
- **Right-click in the air** to flip the shared on/off state and apply it to every linked laser.
- Sneak-use passes through (does nothing) so it does not conflict with other interactions.
- Linked positions and the on/off flag are stored as item data components.

Implementation: [`RemoteControlItem`](reference/items.md#remotecontrolitem),
[`ComponentRegistry`](reference/registries.md#componentregistry).

## Laser Stopper

A block that **blocks lasers** while being able to *disguise* itself as any other block.

- **Use a block item on it** to make it take on that block's model and collision shape.
- It is a laser receptor that absorbs energy without digging, so beams stop at it.
- Its disguise block state is saved to NBT and rendered by a custom renderer.

Implementation: [`LaserStopper`](reference/blocks.md#laserstopper),
[`LaserStopperBlockEntity`](reference/block-entities.md#laserstopperblockentity),
[`LaserStopperRenderer`](reference/client.md#laserstopperrenderer).

## Laser Splitter

A configurable **beam splitter**. It receives energy like a laser receptor, then re-emits it as
several beams out of the sides **perpendicular** to its facing axis.

- **Sneak + right-click with a Mekanism Configurator** to cycle the split mode:
  corner (1 beam) → 2-way → 3-way → 4-way. Right-click (no sneak) prints the current mode.
- The incoming energy is divided evenly between the active outputs; each output beam is a faithful
  laser (damages entities, deposits into receptors, or digs blocks).
- The split mode is saved to NBT.

Implementation: [`LaserSplitter`](reference/blocks.md#lasersplitter),
[`LaserSplitterBlockEntity`](reference/block-entities.md#lasersplitterblockentity).

## Energy Transformer

A laser receptor that simply **stores** the energy it receives (its energy container both accepts and
provides), acting as an adapter between the laser network and normal Mekanism/Forge energy
transfer. Used, for example, as the power input of higher-tier ODECM structures. It does no
per-tick logic.

Implementation: [`EnergyTransformer`](reference/blocks.md#energytransformer),
[`EnergyTransformerBlockEntity`](reference/block-entities.md#energytransformerblockentity).

## ODECM

The **Ore Deposit Extraction & Conversion Machine** — a multi-tier multiblock that fabricates a
chosen ore over time. The ODECM *core* block is the controller; you build one of four structure
"versions" (v1–v4) around it out of Mekanism machines and decoration.

### Setup & controls

- **Right-click the core with an ore block item** to select which ore to generate (must be in the
  `c:ores` tag and not [blacklisted](configuration.md#ore-selection-shared)). The selected ore floats
  above the core (custom renderer).
- **Sneak + right-click with a Configurator** to cycle the targeted generator version (v1→v4→v1).
- **Right-click (no item)** to print progress / build status.
- **Creative right-click with a Configurator** force-builds the currently targeted structure around
  the core (does not overwrite the core itself).

### Operation

Each tick, if the structure is valid and an ore is selected, the core consumes resources according
to the current version's [ability](reference/multiblock.md#generatorability):

- **Power** is pulled from the structure's energy input block (energy cube or energy transformer).
- **Water** is consumed from a fluid tank (higher tiers only).
- **Steam** is output into a chemical tank (higher tiers only).
- When all requirements are met, progress accrues; on completing a cycle, ore is inserted into the
  structure's Personal Barrel output.

Energy cost per tick, water/steam rates, cycle length, and per-version speed multipliers are all
[configurable](configuration.md#odecm). Higher versions are faster but more expensive.

### Implosion (malfunction)

If the ODECM has power but is **starved of water or steam**, and `ODECM_CanExplode` is on, it
triggers an **implosion** instead of exploding: it rips loose blocks within
[`ODECM_Explosion`](configuration.md#odecm) blocks out of the world, sucks them toward the core for a
moment, then blasts them back outward — destroying a fraction of them. Blocks with a block entity are
skipped unless [whitelisted](configuration.md#odecm) (structural glass by default); the
[blacklist](configuration.md#odecm) always wins. A cooldown prevents it from re-triggering every tick.

Implementation: [`ODECM_Block`](reference/blocks.md#odecm_block),
[`ODECM_BlockEntity`](reference/block-entities.md#odecm_blockentity),
[`OreGeneratorBase`](reference/multiblock.md#oregeneratorbase),
[`ODECM_Renderer`](reference/client.md#odecm_renderer).

## Ore Generator (legacy)

A standalone (non-multiblock) machine that predates the ODECM. It buffers laser energy and, on a
cooldown, fabricates a random non-blacklisted ore into its inventory, spending energy per ore. It has
a Mekanism GUI with an energy gauge. Considered deprecated in favour of the ODECM.

Implementation: [`OreGenerator`](reference/blocks.md#oregenerator),
[`OreGeneratorBlockEntity`](reference/block-entities.md#oregeneratorblockentity),
[`OreGeneratorScreen`](reference/client.md#oregeneratorscreen).

## Energy Storage multiblock

An Induction-Matrix-style **energy storage** structure built from three blocks:

- **Energy Storage Casing** — the frame/shell.
- **Energy Storage Port** — input/output. **Sneak + right-click** it with a Configurator to toggle
  between input and output mode. It is also a laser receptor, so a laser can charge the structure.
- **Energy Storage Cell** — placed inside; each cell adds storage capacity and transfer throughput.

The structure is a fixed **3×4×3** cuboid. Once formed, power inserted at a port (in input mode)
distributes into the cells, and a port in output mode ejects into adjacent energy acceptors.

Crafting: casing, port, and cell all have cheap, glass-based recipes.

Implementation: [`energyStorageMultiblockData`](reference/multiblock.md#energystoragemultiblockdata),
[`energyStorageMultiblockContainer`](reference/multiblock.md#energystoragemultiblockcontainer),
[`energyStorageMultiblockValidator`](reference/multiblock.md#energystoragemultiblockvalidator),
[`EnergyStorage*BlockEntity`](reference/block-entities.md#energy-storage-block-entities).

## Interface Block

A block that turns a **redstone pulse** into a Remote Control activation. Put a Remote Control into
its single input slot; on a rising redstone edge (any side), it invokes the remote's toggle for the
block's owner, flipping all lasers linked to that remote. Has a simple Mekanism GUI.

Implementation: [`InterfaceBlock`](reference/blocks.md#interfaceblock),
[`InterfaceBlockEntity`](reference/block-entities.md#interfaceblockentity),
[`InterfaceBlockScreen`](reference/client.md#interfaceblockscreen).

## Laser Configurator

An item that opens an (currently title-only) GUI when used on any of this mod's laser blocks (but not
Mekanism's own `mekanism:laser`). Intended as the future home for per-laser configuration.

Implementation: [`LaserConfiguratorItem`](reference/items.md#laserconfiguratoritem),
[`LaserConfiguratorContainer`](reference/multiblock.md#laserconfiguratorcontainer),
[`LaserConfiguratorItemScreen`](reference/client.md#laserconfiguratoritemscreen).

## Energized Fence

A decorative/structural block with a thin (fence-post-sized) collision shape, used as a component of
the larger ODECM structures. No block entity.

Implementation: registered inline in [`BlockRegistry`](reference/registries.md#blockregistry).
