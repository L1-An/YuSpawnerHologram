package com.github.l1an.yuspawnerhologram

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

    override fun onLoad() {
        Language.default = "zh_CN"
    }

    override fun onEnable() {
        Metrics(20123, BukkitPlugin.getInstance().description.version, Platform.BUKKIT)

        console().sendMessage("&aYuSpawnerHologram has been loaded!".colored())
        console().sendMessage("&bAuthor by L1An".colored())
    }
}