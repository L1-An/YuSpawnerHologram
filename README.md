# Powered by TabooLib6
* [TabooLib6](https://github.com/TabooLib/taboolib)

# Info
[![wakatime](https://wakatime.com/badge/github/L1-An/YuSpawnerHologram.svg)](https://wakatime.com/badge/github/L1-An/YuSpawnerHologram)

> Allows MythicMobs' spawner to visualize warmup time.  

Dependency: `MythicMobs`  
SoftDependency: `PlaceholderAPI, HolographicDisplays, DecentHolograms, Adyeshach`

[Spigot link](https://www.spigotmc.org/resources/yuspawnerhologram-%E2%9C%85for-mythicspawner-display-refresh-time-%E2%9C%85intelligent-multilingual-support.113207/)

# Usage

## Command
`yuspawnerhologram / spawnerholo / mmholo`

## PlaceHolder
|         Placeholder         |                  Description                   |
|:---------------------------:|:----------------------------------------------:|
| %spawnerholo_{spawnerName}% | Gets the respawn time of the specified spawner |

# Building

* [Gradle](https://gradle.org/) - Dependency Management

The GradleWrapper in included in this project.

**Windows:**

```
gradlew.bat clean build
```

**macOS/Linux:**

```
./gradlew clean build
```

Build artifacts should be found in `./build/libs` folder.