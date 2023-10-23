package com.github.l1an.yuspawnerhologram.internal.core.mythichologram

import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.createAllHologramByHD
import me.filoghost.holographicdisplays.api.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

object HologramEnter {
    // 获取 holographicDisplay 插件实例
    val holographicDisplays = Bukkit.getPluginManager().getPlugin("HolographicDisplays")
    val decentHolograms = Bukkit.getPluginManager().getPlugin("DecentHolograms")
    val adyeshach = Bukkit.getPluginManager().getPlugin("Adyeshach")
    // 获取 YuSpawnerHologram 插件实例
    val YuSpawnerHologram : Plugin = Bukkit.getPluginManager().getPlugin("YuSpawnerHologram")!!

    @Awake(LifeCycle.ACTIVE)
    fun checkPlugin() {
        when {
            /**
            adyeshach != null -> {
                console().sendLang("found-dependency", "Adyeshach")
                createAllHologram(Bukkit.getConsoleSender(), "Adyeshach")
            }
            decentHolograms != null -> {
                console().sendLang("found-dependency", "DecentHolograms")
                createAllHologram(Bukkit.getConsoleSender(), "DecentHolograms")
            }
            */
            holographicDisplays != null -> {
                console().sendLang("dependency-found", "HolographicDisplays")
                createAllHologramByHD(Bukkit.getConsoleSender(), "HolographicDisplays")
            }
            else -> {
                console().sendLang("dependency-not-found")
            }
        }
    }
}