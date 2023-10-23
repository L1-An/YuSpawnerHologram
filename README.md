# Powered by TabooLib6
* [TabooLib6](https://github.com/TabooLib/taboolib)

# Info
[![wakatime](https://wakatime.com/badge/github/L1-An/YuSpawnerHologram.svg)](https://wakatime.com/badge/github/L1-An/YuSpawnerHologram)

> Allows MythicMobs' spawner to visualize warmup time.  

Dependency: `MythicMobs`  
SoftDependency: `PlaceholderAPI, HolographicDisplays, DecentHolograms, Adyeshach`

# Usage

## Command
`yuspawnerhologram / spawnerholo / mmholo`

## PlaceHolder
|         Placeholder         |    Description     |
|:---------------------------:|:------------------:|
| %spawnerholo_{spawnerName}% | 获取指定 spawner 的重生时间 |

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