package com.github.l1an.yuspawnerhologram.internal.core.mythichologram

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig
import com.github.l1an.yuspawnerhologram.internal.manager.HologramTextContainer
import com.github.l1an.yuspawnerhologram.internal.manager.HologramType
import com.github.l1an.yuspawnerhologram.util.MythicHologramUtils
import com.github.l1an.yuspawnerhologram.util.TimeUtils
import com.github.l1an.yuspawnerhologram.util.Utils
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI
import me.filoghost.holographicdisplays.api.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.common5.mirrorNow
import taboolib.module.configuration.Configuration
import taboolib.platform.util.sendLang

object HolographicHologram {
    // 创建一个 map 用于存储 hologram 和其 name
    val holograms = mutableMapOf<String, Hologram>()

    /**
     * 从配置文件中创建 hologram
     * @return 创建出 hologram 并将其存入 Map : holograms 中
     * @author L1An
     * @since 2023/10/21
     */
    fun createAllHologramByHD(sender : CommandSender, plugin : String) {
        mirrorNow("Initialize Holograms") {
            val keys = Utils.getConfigKeys(YuSpawnerHologramConfig.config, "hologramText")
            var createdHologramsCount = 0 // 用于跟踪成功创建的hologram的数量

            for (spawnerName in keys) {
                val hologram = createHologram(spawnerName)
                if (hologram != null) {
                    createdHologramsCount++  // 成功创建hologram，增加计数器
                }
            }
            sender.sendLang("holo-refresh-all-success", createdHologramsCount)
        }
    }

    /**
     * 创建 hologram
     * @param name hologram 的名字
     * @return 创建出 hologram 并将其存入 Map : hologramsByName 中，如果 hologram 已存在或 HolographicDisplays 未安装则返回 null
     * @author L1An
     * @since 2023/10/21
     */
    private fun createHologram(name : String, sender : CommandSender = Bukkit.getConsoleSender()) : Hologram? {
        if (holograms.containsKey(name)) {
            // 名字已存在
            sender.sendLang("spawner-already-exist", name)
            return null
        }
        if (HologramEnter.holographicDisplays != null) {
            val spawner = HookMythicMobs.getSpawnerManager(name)

            // 若 spawner 不存在则返回 null
            if (spawner == null) {
                sender.sendLang("no-spawner", name)
                return null
            }

            // 若 hologram 已存在则返回 null
            if (holograms.containsKey(name)) {
                sender.sendLang("spawner-already-exist", name)
                return null
            }

            // 创建 hologram
            val world = Bukkit.getWorld(spawner.worldName)
            val location = Location(world, spawner.location.x, spawner.location.y + 3, spawner.location.z)
            val hologram = HolographicDisplaysAPI.get(HologramEnter.YuSpawnerHologram).createHologram(location)

            // 存储并返回 hologram
            holograms[name] = hologram
            refreshHologramText(name)
            return hologram
        } else {
            sender.sendLang("hd-not-found")
            return null
        }
    }

    /**
     * 根据名字获取 hologram
     * @param name hologram 的名字
     * @return 返回 hologram 对象，如果 hologram 不存在则返回 null
     * @author L1An
     * @since 2023/10/21
     */
    private fun getHologram(name : String) : Hologram? {
        return holograms[name]
    }

    /**
     * 根据名字删除 hologram
     * @param name hologram 的名字
     * @return 删除 hologram 并将其从 Map : hologramsByName 中移除，如果 hologram 不存在则返回 null
     * @author L1An
     * @since 2023/10/21
     */
    private fun deleteHologram(name : String) {
        getHologram(name)?.let {
            it.delete()
            holograms.remove(name)
        }
    }

    /**
     * 根据名字刷新 hologram
     * @param name hologram 的名字
     * @return 刷新 hologram 的文本
     * @author L1An
     * @since 2023/10/22
     */
    fun refreshHologram(name : String, sender : CommandSender) {
        mirrorNow("Refresh Hologram") {
            deleteHologram(name)
            val hologram = createHologram(name, sender)
            if (hologram != null) {
                sender.sendLang("holo-refresh-success", name)
            } else {
                sender.sendLang("holo-refresh-fail", name)
            }
        }
    }

    /**
     * 根据名字刷新 hologram 的文本
     * @param name hologram 的名字
     * @author L1An
     * @since 2023/10/21
     */
    private fun refreshHologramText(name : String) : Boolean {
        val spawner = HookMythicMobs.getSpawnerManager(name)
        if (spawner != null) {
            val texts = getHologramText(
                YuSpawnerHologramConfig.config,
                "hologramText",
                name,
                MythicHologramUtils.getDisplayNameFromConfigs(spawner.typeName)!!,
                spawner.cooldownSeconds,
                spawner.warmupSeconds
            )

            // 错误检查: 检查获取的文本是否为空
            if (texts.isEmpty()) {
                println("Error: 从${name}获取到的文本是空的")
            }

            val hologram = holograms[name]
            if (hologram != null) {
                val lines = hologram.lines
                lines.clear()

                for (text in texts) {
                    lines.appendText(text)
                }
                return true
            }
        } else {
            // 错误检查: 如果 spawner 不存在，则打印错误
            println("Error: 未找到名为 $name 的 spawner")
        }
        return false
    }

    /**
     * 获取配置文件中的列表
     * @param config 配置文件对象
     * @param section 配置文件中顶层 key，例如 hologramText
     * @param key section中的 key，例如 default，一般是通过 getConfigKeys 方法获取的key
     * @param mobName 生物名称，用于替换占位符 %name%
     * @param cooldownSeconds 重生时间，用于替换占位符 %cooldown%
     * @param warmupSeconds 准备时间，用于替换占位符 %warmup%
     * @return 返回 key 中的值，为列表形式
     * @author L1An
     * @since 2023/10/21
     */
    private fun getHologramText(
        config : Configuration,
        section : String,
        key : String,
        mobName : String,
        cooldownSeconds : Int,
        warmupSeconds : Int
    ) : List<String> {
        return getHologramTextWithInfo(config, section, key, mobName, cooldownSeconds, warmupSeconds).map { it.text }
    }

    /**
     * 获取配置文件中的列表
     * @param config 配置文件对象
     * @param section 配置文件中顶层 key，例如 hologramText
     * @param key section中的 key，例如 default，一般是通过 getConfigKeys 方法获取的key
     * @param mobName 生物名称，用于替换占位符 %name%
     * @param cooldownSeconds 重生时间，用于替换占位符 %cooldown%，但现暂时不用
     * @param warmupSeconds 准备时间，用于替换占位符 %warmup%
     * @return 返回 key 中的值，为<HologramTextContainer>列表形式，同时返回每行的类型(HologramType)，用于判断是否需要刷新 %cooldown% 或 %warmup% 的占位符
     */
    fun getHologramTextWithInfo(
        config : Configuration,
        section : String,
        key : String,
        mobName : String,
        cooldownSeconds : Int,
        warmupSeconds : Int
    ) : List<HologramTextContainer> {
        val cooldown = TimeUtils.secondToFormat(config, cooldownSeconds, "durationFormat")
        val warmup = TimeUtils.secondToFormat(config, warmupSeconds, "durationFormat")
        val texts = config.getStringList("$section.$key") ?: listOf()

        return texts.map {
            val modifiedText = it.replace("%name%", mobName).replace("&", "§")
            when {
                modifiedText.contains("%cooldown%") -> HologramTextContainer(modifiedText.replace("%cooldown%", cooldown), HologramType.COOLDOWN)
                modifiedText.contains("%warmup%") -> HologramTextContainer(modifiedText.replace("%warmup%", warmup), HologramType.WARMUP)
                else -> HologramTextContainer(modifiedText, HologramType.NEUTRAL)
            }
        }
    }
}