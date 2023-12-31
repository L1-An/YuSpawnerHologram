package com.github.l1an.yuspawnerhologram.module.holographicdisplays

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter
import com.github.l1an.yuspawnerhologram.internal.manager.HologramTextContainer
import com.github.l1an.yuspawnerhologram.internal.manager.HologramType
import com.github.l1an.yuspawnerhologram.internal.util.MythicHologramUtils.getDisplayNameFromConfigs
import com.github.l1an.yuspawnerhologram.util.TimeUtils.secondToFormat
import com.github.l1an.yuspawnerhologram.util.Utils.getConfigKeys
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
    fun createAllHologramByHD(sender : CommandSender) {
        mirrorNow("Initialize Holograms") {
            val keys = getConfigKeys(config, "hologramText")
            var createdHologramsCount = 0 // 用于跟踪成功创建的 hologram 的数量

            for (spawnerName in keys) {
                val hologram = createHologramByHD(spawnerName)
                if (hologram != null) createdHologramsCount++  // 成功创建hologram，增加计数器
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
    private fun createHologramByHD(name : String, sender : CommandSender = Bukkit.getConsoleSender()) : Hologram? {
        if (holograms.containsKey(name)) {
            // 名字已存在
            sender.sendLang("spawner-already-exist", name)
            return null
        }
        val spawner = getSpawnerManager(name)

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
        refreshHologramTextByHD(name)
        return hologram
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
    fun refreshHologramByHD(name : String, sender : CommandSender) {
        mirrorNow("Refresh Hologram") {
            deleteHologram(name)
            val hologram = createHologramByHD(name, sender)
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
    private fun refreshHologramTextByHD(name : String) : Boolean {
        val spawner = getSpawnerManager(name)!!
        val texts = getHologramText(
            config,
            name,
            getDisplayNameFromConfigs(spawner.typeName)!!,
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
        key : String,
        mobName : String,
        warmupSeconds : Int
    ) : List<String> {
        return getHologramTextWithInfo(config, key, mobName, warmupSeconds).map { it.text }
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
        key : String,
        mobName : String,
        warmupSeconds : Int
    ) : List<HologramTextContainer> {
        val warmup = secondToFormat(config, warmupSeconds, "durationFormat")
        val texts = config.getStringList("hologramText.$key") ?: listOf()

        return texts.map {
            val modifiedText = it.replace("%name%", mobName).replace("&", "§")
            when {
                modifiedText.contains("%warmup%") -> HologramTextContainer(modifiedText.replace("%warmup%", warmup), HologramType.WARMUP)
                else -> HologramTextContainer(modifiedText, HologramType.NEUTRAL)
            }
        }
    }
}