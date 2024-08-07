![Logo](https://raw.githubusercontent.com/L1-An/YuSpawnerHologram/main/.github/images/logo.png)

![GitHub Release](https://img.shields.io/github/v/release/L1-An/YuSpawnerHologram?style=for-the-badge&logo=VirusTotal)
![GitHub last commit (branch)](https://img.shields.io/github/last-commit/L1-An/YuSpawnerHologram/main?style=for-the-badge&logo=prisma)

# Powered by TabooLib6
* [TabooLib6](https://github.com/TabooLib/taboolib)

# Info
![wakatime](https://wakatime.com/badge/github/L1-An/YuSpawnerHologram.svg)

> Allows MythicMobs' spawner to visualize warmup time.  

> To facilitate the statistics of download volume, please go to [SpigotMC](https://www.spigotmc.org/resources/yuspawnerhologram-%E2%9C%85for-mythicspawner-display-refresh-time-%E2%9C%85intelligent-multilingual-support.113207/) to download the plugin.

Dependency: `MythicMobs`  
SoftDependency: `PlaceholderAPI, HolographicDisplays, DecentHolograms, Adyeshach`

## Related Links
[SpigotMC](https://www.spigotmc.org/resources/yuspawnerhologram-%E2%9C%85for-mythicspawner-display-refresh-time-%E2%9C%85intelligent-multilingual-support.113207/)  
[巴哈姆特](https://forum.gamer.com.tw/C.php?bsn=18673&snA=200464&subbsn=14&page=1&s_author=&gothis=1062160#1062160)  
[Discord Community](https://discord.gg/SzPBHGttaR)

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
