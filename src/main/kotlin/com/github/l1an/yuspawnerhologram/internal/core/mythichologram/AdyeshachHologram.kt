package com.github.l1an.yuspawnerhologram.internal.core.mythichologram

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.HologramUpdateSubmit
import com.github.l1an.yuspawnerhologram.internal.util.MythicHologramUtils.getDisplayNameFromConfigs
import com.github.l1an.yuspawnerhologram.util.TimeUtils
import com.github.l1an.yuspawnerhologram.util.Utils.getConfigKeys
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.AdyeshachHologram
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.common5.mirrorNow
import taboolib.module.configuration.Configuration
import taboolib.platform.util.sendLang

/**
 * 处理为 Adyeshach 支持的 Hologram
 * @author L1An
 * @since 2023/10/24
 */
object AdyeshachHologram {
    // 创建一个 map 用于存储 AdyeshachHologram 和其 name
    private val holograms = mutableMapOf<String, AdyeshachHologram>()
    private val api = Adyeshach.api().getHologramHandler()

    /**
     * 从配置文件中创建所有 hologram
     * @return 创建出 hologram 并将其存入 Map : holograms 中
     */
    fun createAllHologramByADY(sender : CommandSender) {
        mirrorNow("Initialize Holograms") {
            val keys = getConfigKeys(config, "hologramText")
            var createdHologramCount = 0 // 用于跟踪成功创建的 hologram 的数量

            for (spawnerName in keys) {
                val hologram = createHologramByADY(spawnerName, sender)
                if (hologram != null) createdHologramCount++
            }
            sender.sendLang("holo-refresh-all-success", createdHologramCount)
        }
    }

    /**
     * 创建 hologram
     * @param name hologram 的名字
     * @return 创建出 hologram 并将其存入 Map : hologramsByName 中
     */
    private fun createHologramByADY(name : String, sender : CommandSender = Bukkit.getConsoleSender(), tip : Boolean = true) : AdyeshachHologram? {
        if (holograms.containsKey(name) && tip) {
            sender.sendLang("spawner-already-exist", name)
            return null
        }
        val spawner = getSpawnerManager(name)

        if (spawner == null) {
            sender.sendLang("no-spawner", name)
            return null
        }

        val location = Location(Bukkit.getWorld(spawner.worldName), spawner.location.x, spawner.location.y + 2, spawner.location.z)
        val texts = getHologramTextForADY(
            config,
            name,
            getDisplayNameFromConfigs(spawner.typeName)!!,
            spawner.remainingWarmupSeconds
        )
        val hologram = api.createHologram(location, texts)

        // 存储并返回 hologram
        holograms[name] = hologram
        return hologram
    }

    /**
     * 刷新 hologram (删除后重新添加)
     * @param spawnerName hologram 的名字
     * @return 刷新 hologram 并返回信息
     */
    fun refreshHologramByADY(spawnerName : String, sender : CommandSender = Bukkit.getConsoleSender(), tip : Boolean = false) {
        mirrorNow("Refresh Hologram") {
            val hologram = holograms[spawnerName]
            if (hologram != null) {
                hologram.remove()
                createHologramByADY(spawnerName, sender, tip)
                sender.sendLang("holo-refresh-success", spawnerName)
            } else {
                sender.sendLang("holo-refresh-fail", spawnerName)
            }
        }
    }

    /**
     * 刷新 hologram 的内容
     * @param spawnerName hologram 的名字
     */
    fun refreshHologramTextByADY(spawnerName : String) {
        val hologram = holograms[spawnerName]
        val spawner = getSpawnerManager(spawnerName) ?: return
        val texts = getHologramTextForADY(
            config,
            spawnerName,
            getDisplayNameFromConfigs(spawner.typeName)!!,
            spawner.remainingWarmupSeconds
        )
        hologram?.updateSafely(texts)
    }

    /**
     * 从配置文件获取 hologram 的文本
     * @param config 配置文件
     * @param key hologram 的名字
     * @param mobName 怪物的索引名
     * @param warmupSeconds 怪物的预热时间
     * @return 返回 hologram 的文本
     */
    private fun getHologramTextForADY(
        config : Configuration,
        key : String,
        mobName : String,
        warmupSeconds : Int
    ) : List<String> {
        val warmup = TimeUtils.secondToFormat(config, warmupSeconds, "durationFormat")
        val texts = config.getStringList("hologramText.$key") ?: listOf()
        val spawner = getSpawnerManager(key)

        return texts.map {
            var modifiedText = it.replace("%name%", mobName).replace("&", "§")
            if (modifiedText.contains("%warmup%")) {
                modifiedText = if (spawner?.isOnWarmup == true) {
                    modifiedText.replace("%warmup%", warmup)
                } else {
                    modifiedText.replace("%warmup%", HologramUpdateSubmit.activeMsg)
                }
            }
            modifiedText
        }
    }
}