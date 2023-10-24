package com.github.l1an.yuspawnerhologram.internal.core.mythichologram

import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.AdyeshachHologram.createAllHologramByADY
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.DecentHologram.createAllHologramByDH
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
            adyeshach != null -> {
                console().sendLang("dependency-found", "Adyeshach")
                createAllHologramByADY(Bukkit.getConsoleSender())
            }
            decentHolograms != null -> {
                console().sendLang("dependency-found", "DecentHolograms")
                createAllHologramByDH(Bukkit.getConsoleSender())
            }
            holographicDisplays != null -> {
                console().sendLang("dependency-found", "HolographicDisplays")
                createAllHologramByHD(Bukkit.getConsoleSender())
            }
            else -> {
                console().sendLang("dependency-not-found")
                Bukkit.getPluginManager().disablePlugin(YuSpawnerHologram)
            }
        }
    }
}