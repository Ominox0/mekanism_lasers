# mekanism_lasers — Documentation

This folder documents **every** feature, class, and method in the mod. It is meant to be kept in
sync with the code: when a class, method, or config option changes, update the matching section here
in the same change.

## Contents

| Document | What it covers |
| --- | --- |
| [features.md](features.md) | Every block and item, what it does in-game, and how to use it |
| [configuration.md](configuration.md) | Every entry in the config file, with defaults and ranges |
| [reference/core.md](reference/core.md) | Mod entrypoint (`Mekanism_lasers`) and `Config` |
| [reference/registries.md](reference/registries.md) | All deferred registers (blocks, block entities, items, containers, components, damage types) |
| [reference/blocks.md](reference/blocks.md) | `Block` subclasses and shapes |
| [reference/block-entities.md](reference/block-entities.md) | Every `BlockEntity` and its logic |
| [reference/items.md](reference/items.md) | Items and block-items |
| [reference/multiblock.md](reference/multiblock.md) | ODECM structure definitions + Energy Storage multiblock |
| [reference/client.md](reference/client.md) | Renderers, screens, and client setup |

## High-level overview

The mod is a Mekanism add-on and reuses Mekanism's block/tile framework heavily
(`BlockTypeTile`, `TileEntityMekanism`, `LaserEnergyContainer`, the multiblock framework, and the
Mekanism GUI system). Almost every block is registered as a Mekanism *block type* with attributes,
and almost every block entity extends a Mekanism base class.

### Package layout

```
com.folumo.mekanism_lasers
├── Mekanism_lasers          Mod entrypoint (@Mod), registers all deferred registers + config
├── Config                   All ModConfigSpec options
├── client
│   ├── ClientSetup          Registers BER renderers + menu screens (client event bus)
│   ├── gui/                 Mekanism-style GUI screens
│   └── renderer/            BlockEntityRenderers (ODECM ore preview, Laser Stopper disguise)
└── common
    ├── block/               Block classes + voxel shapes
    ├── block_entity/        BlockEntity (tile) classes
    ├── container/           Menu/container + item-inventory helpers
    ├── item/                Items and tiered block-items
    ├── lang/                ILangEntry enum for translation keys
    ├── multiblock/          ODECM structure definitions + Energy Storage multiblock data/validator
    ├── registry/            Deferred registers (blocks, tiles, items, containers, components, damage)
    └── tier/                LaserTier enum (BASIC..CREATIVE)
```

### Registration flow

`Mekanism_lasers` (the `@Mod` class) registers, on the mod event bus:

1. `BlockRegistry.BLOCKS` — blocks + their block-items.
2. `BlockEntityTypeRegistry.BLOCK_ENTITY_TYPES` — tile entity types, tickers, and capabilities.
3. `ContainerTypeRegistry.CONTAINER_TYPES` — GUI menu types.
4. `ItemRegistry.ITEMS` and the creative tab.
5. The common config spec (`Config.SPEC`).

`BlockTypeRegistry` ties blocks to tiles by building Mekanism `BlockTypeTile` descriptors (shape,
attributes, energy config, GUI, sound). Client-only wiring (renderers, screens) is done in
`ClientSetup` on the mod's client event bus.

### Keeping this in sync

When you change code, update docs in the same commit:

- New/removed/renamed **class** → update the relevant `reference/*.md` file and, if it is a
  player-facing block/item, `features.md`.
- New/removed **config option** → update `configuration.md`.
- Behavioural change to a method → update its description in the matching `reference/*.md`.
