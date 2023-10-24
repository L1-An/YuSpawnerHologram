package com.github.l1an.yuspawnerhologram.internal.command.subcommand

import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.AdyeshachHologram
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.DecentHologram
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram
import com.github.l1an.yuspawnerhologram.internal.manager.ConfigManager
import com.github.l1an.yuspawnerhologram.util.Utils
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common5.mirrorNow
import taboolib.platform.util.sendLang

val CommandReload = subCommand {
    execute<CommandSender> {sender, _, _ ->
        mirrorNow("Plugin Reload") {
            val keys = Utils.getConfigKeys(YuSpawnerHologramConfig.config, "hologramText")
            for (spawnerName in keys) {
                when {
                    HologramEnter.adyeshach != null -> AdyeshachHologram.refreshHologramByADY(spawnerName, sender)
                    HologramEnter.decentHolograms != null -> DecentHologram.refreshHologramByDH(spawnerName, sender)
                    HologramEnter.holographicDisplays != null -> HolographicHologram.refreshHologramByHD(spawnerName, sender)
                    else -> sender.sendLang("dependency-not-found")
                }
            }
            sender.sendLang("command-reload")
            ConfigManager.reload()
        }
    }
}