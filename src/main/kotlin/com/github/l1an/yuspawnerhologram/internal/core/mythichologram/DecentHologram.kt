package com.github.l1an.yuspawnerhologram.internal.core.mythichologram

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.HologramUpdateSubmit.activeMsg
import com.github.l1an.yuspawnerhologram.internal.util.MythicHologramUtils.getDisplayNameFromConfigs
import com.github.l1an.yuspawnerhologram.util.TimeUtils.secondToFormat
import com.github.l1an.yuspawnerhologram.util.Utils.getConfigKeys
import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.common5.mirrorNow
import taboolib.module.configuration.Configuration
import taboolib.platform.util.sendLang

/**
 * 处理为 DecentHolograms 支持的 Hologram
 * @author L1An
 * @since 2023/10/24
 */
object DecentHologram {
    /**
     * 从配置文件中创建所有 hologram
     * @return 创建出 hologram 并返回信息
     */
    fun createAllHologramByDH(sender : CommandSender) {
        mirrorNow("Initialize Holograms") {
            val keys = getConfigKeys(config, "hologramText")
            var createdHologramsCount = 0 // 用于跟踪成功创建的hologram的数量

            for (spawnerName in keys) {
                createHologramByDH(spawnerName)
                createdHologramsCount++
            }
            sender.sendLang("holo-refresh-all-success", createdHologramsCount)
        }
    }

    /**
     * 创建 hologram
     * @param spawnerName hologram 的名字
     * @return 创建出 hologram
     */
    private fun createHologramByDH(spawnerName : String, sender : CommandSender = Bukkit.getConsoleSender()) {
        val spawner = getSpawnerManager(spawnerName)
        // 若 spawner 不存在则返回 null
        if (spawner == null) {
            sender.sendLang("no-spawner", spawnerName)
            return
        }

        val location = Location(Bukkit.getWorld(spawner.worldName), spawner.location.x, spawner.location.y + 3, spawner.location.z)
        val texts = getHologramTextForDH(
            config,
            spawnerName,
            getDisplayNameFromConfigs(spawner.typeName)!!,
            spawner.remainingWarmupSeconds
        )
        DHAPI.createHologram(spawnerName, location, texts)
    }

    /**
     * 刷新 hologram (删除后重新添加)
     * @param spawnerName hologram 的名字
     * @return 刷新 hologram 并返回信息
     */
    fun refreshHologramByDH(spawnerName : String, sender : CommandSender = Bukkit.getConsoleSender()) {
        mirrorNow("Refresh Hologram") {
            val hologram = DHAPI.getHologram(spawnerName)
            if (hologram != null) {
                hologram.delete()
                createHologramByDH(spawnerName, sender)
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
    fun refreshHologramTextByDH(spawnerName: String) {
        val hologram = DHAPI.getHologram(spawnerName)
        val spawner = getSpawnerManager(spawnerName) ?: return
        val texts = getHologramTextForDH(
            config,
            spawnerName,
            getDisplayNameFromConfigs(spawner.typeName)!!,
            spawner.remainingWarmupSeconds
        )
        if (hologram != null) {
            DHAPI.setHologramLines(hologram, texts)
        }
    }

    /**
     * 从配置文件获取 hologram 的文本
     * @param config 配置文件
     * @param key hologram 的名字
     * @param mobName 怪物的索引名
     * @param warmupSeconds 怪物的预热时间
     * @return 返回 hologram 的文本
     */
    private fun getHologramTextForDH(
        config : Configuration,
        key : String,
        mobName : String,
        warmupSeconds : Int
    ) : List<String> {
        val warmup = secondToFormat(config, warmupSeconds, "durationFormat")
        val texts = config.getStringList("hologramText.$key") ?: listOf()
        val spawner = getSpawnerManager(key)

        return texts.map {
            var modifiedText = it.replace("%name%", mobName).replace("&", "§")
            if (modifiedText.contains("%warmup%")) {
                modifiedText = if (spawner?.isOnWarmup == true) {
                    modifiedText.replace("%warmup%", warmup)
                } else {
                    modifiedText.replace("%warmup%", activeMsg)
                }
            }
            modifiedText
        }
    }
}