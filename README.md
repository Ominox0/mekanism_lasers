# mekanism_lasers

A [Mekanism](https://github.com/mekanism/Mekanism) add-on for Minecraft 1.21.1 / NeoForge that adds
tiered laser emitters and a family of laser-driven machines: laser utility blocks (stopper, splitter,
energy transformer), the **ODECM** ore-fabrication multiblock, an **Energy Storage** multiblock, an
**Interface Block**, and supporting items (Remote Control, Laser Configurator).

## Documentation

Full documentation lives in [`docs/`](docs/):

- [Documentation index & overview](docs/README.md)
- [Feature guide](docs/features.md) — what every block and item does in-game
- [Configuration reference](docs/configuration.md) — every config option
- [Code reference](docs/reference/) — every class and method, by package

## Building

There is no `gradlew` wrapper script checked in. Build with a system Gradle **8.13+** (8.14 is known
good; 8.9 fails because the NeoGradle plugin needs a newer Gradle):

```sh
gradle compileJava      # compile
gradle build            # full build / jar
gradle runClient        # launch the dev client
```
