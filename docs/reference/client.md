# Reference — Client

Client-only code in `client`: setup, block-entity renderers, and GUI screens. All of this runs only on
the physical client (`Dist.CLIENT`).

---

## `ClientSetup`

`client.ClientSetup` — `@EventBusSubscriber(Dist.CLIENT)` on the mod event bus.
- `registerRenderers(event)` — registers block-entity renderers: `LaserStopperRenderer` for the Laser
  Stopper and `ODECM_Renderer` for the ODECM.
- `registerContainers(event)` — registers menu screens for the Ore Generator, Interface Block, and
  Laser Configurator container types.

---

## `ODECM_Renderer`

`client.renderer.ODECM_Renderer implements BlockEntityRenderer<ODECM_BlockEntity>`. Draws the selected
target ore floating above the ODECM core.
- `render(...)` — if an ore is selected, pushes a scaled/translated pose, tessellates the ore block
  model, and pops. The push/pop is wrapped in `try/finally` so a render exception can never leave the
  pose stack unbalanced (which previously crashed the game with "Pose stack not empty").
- `isHoldingMekanismConfigurator(stack)` — helper to detect a Mekanism Configurator (used by the
  WIP hologram preview).
- `renderMultiblockHolograms(...)` / `renderTransparentBlock(...)` / `rotate(...)` / record
  `BlockPosAndState` — a work-in-progress structure-hologram preview (currently not invoked).

## `LaserStopperRenderer`

`client.renderer.LaserStopperRenderer implements BlockEntityRenderer<LaserStopperBlockEntity>`. Renders
the stopper's **disguise** block (since the block itself uses an `INVISIBLE` render shape).
- `render(...)` → `renderNew(...)` — tessellates the disguise block model (`getCurrentBlockState()`),
  falling back to the stopper's own state when none is set.

## `OreGeneratorScreen`

`client.gui.OreGeneratorScreen extends GuiMekanismTile<OreGeneratorBlockEntity, …>`. The legacy Ore
Generator GUI.
- `addGuiElements()` — adds a standard energy gauge bound to the tile's energy container.
- `drawForegroundText(...)` — renders the title.

## `InterfaceBlockScreen`

`client.gui.InterfaceBlockScreen extends GuiMekanismTile<InterfaceBlockEntity, …>`. Minimal GUI (title
only, plus the tile's inventory slot from the container).

## `LaserConfiguratorItemScreen`

`client.gui.LaserConfiguratorItemScreen extends GuiMekanism<LaserConfiguratorContainer>`. The Laser
Configurator's item GUI — currently just renders the title (the intended home for future per-laser
configuration UI).

---

## Language keys — `MekanismLasersLang`

`common.lang.MekanismLasersLang implements ILangEntry` — an enum of translation keys used by
descriptions and the energy-storage port mode message. Each value builds a Mekanism-style translation
key (`description.mekanism.<path>` etc.); `getTranslationKey()` returns it. The actual English strings
live in `assets/mekanism_lasers/lang/en_us.json`.
