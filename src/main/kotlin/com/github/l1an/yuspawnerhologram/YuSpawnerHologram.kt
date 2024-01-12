package com.github.l1an.yuspawnerhologram

import com.github.l1an.yuspawnerhologram.api.UpdateChecker
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.Language
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

object YuSpawnerHologram : Plugin() {

    val mythicBukkit: MythicBukkit by lazy {
        val mythicMobsPlugin = Bukkit.getPluginManager().getPlugin("MythicMobs")
        mythicMobsPlugin as MythicBukkit
    }

    val messagePrefix = "&f[ &5YuSpawnerHologram &f]"

    override fun onLoad() {
        Language.default = "zh_CN"
    }

    override fun onEnable() {
        Metrics(20123, BukkitPlugin.getInstance().description.version, Platform.BUKKIT)

        console().sendMessage("$messagePrefix &aYuSpawnerHologram has been loaded!".colored())
        console().sendMessage("$messagePrefix &bAuthor by L1An".colored())

        // 检查更新
        UpdateChecker(113207).getVersion { version: String? ->
            if (BukkitPlugin.getInstance().description.version == version) {
                console().sendMessage("$messagePrefix &cThere is not a new update available.".colored())
            } else {
                console().sendMessage("$messagePrefix &aThere is a new update available at:".colored())
                console().sendMessage("&ahttps://www.spigotmc.org/resources/113207/".colored())
            }
        }
    }
}