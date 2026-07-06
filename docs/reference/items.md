# Reference — Items

Items in `common.item`. Block-items for the tiered lasers, plus the Remote Control and Laser
Configurator.

---

## `LaserBlockItem`

`extends ItemBlockTooltip<…>`. Block-item for the tiered `Laser` blocks.
- Resolves the block's `LaserTier` in the constructor.
- `getName(stack)` — colours the item name with the tier's colour.

## `ToggleableLaserBlockItem`

Identical to `LaserBlockItem` but for the `ToggleableLaser` blocks; tier-coloured name.

## `RemoteControlItem`

`extends Item`. Links to and toggles many toggleable lasers at once. See
[features → Remote Control](../features.md#remote-control).

**Constructor** — `UNCOMMON` rarity, stack size 1, and default data components: Mekanism configurator
mode `CONFIGURATE_ITEMS`, an empty `RC_BLOCKPOS` list, and `RC_ACTIVITY = false`.

**Methods**
- `getName(stack)` — aqua-coloured name.
- `use(world, player, hand)` — server-side, non-sneaking: messages "Switching laser mode" and calls
  `switchLaserMode`. Sneaking passes through.
- `useOn(context)` — server-side: if used on a toggleable laser the player owns, links or unlinks that
  position (stored in `RC_BLOCKPOS`) and messages "added"/"removed".
- `switchLaserMode(stack, world, player)` — flips `RC_ACTIVITY` and applies the new on/off state to
  every linked toggleable laser.
- `doesSneakBypassUse(...)` — `false` (so sneak-use still reaches this item).
- Private helpers `saveLinkedLasers`, `getBlockPos`, `getActive` read/write the item's data components.

## `LaserConfiguratorItem`

`extends Item implements IGuiItem`. Opens the Laser Configurator GUI on this mod's lasers. See
[features → Laser Configurator](../features.md#laser-configurator).

**Methods**
- `useOn(context)` — if used on a `LaserBlockEntity` or `ToggleableLaserBlockEntity`, opens the
  configurator's item GUI server-side (`ContainerTypeRegistry.LASER_CONFIGURATOR.tryOpenGui`) and
  returns a sided success; otherwise passes. (Mekanism's own `mekanism:laser` is a different tile, so
  it is not matched.)
- `getContainerType()` — the `LASER_CONFIGURATOR` container type (for `IGuiItem` network encoding).
