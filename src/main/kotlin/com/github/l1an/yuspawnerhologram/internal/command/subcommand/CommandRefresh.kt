package com.github.l1an.yuspawnerhologram.internal.command.subcommand

import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.DecentHologram.refreshHologramByDH
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.adyeshach
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.decentHolograms
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.holographicDisplays
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.refreshHologramByHD
import com.github.l1an.yuspawnerhologram.util.Utils
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.platform.util.sendLang

val CommandRefresh = subCommand {
    execute<CommandSender> { sender, _, _ ->
        val keys = Utils.getConfigKeys(config, "hologramText")
        for (spawnerName in keys) {
            when {
                //adyeshach != null -> refreshHologramByADY(spawnerName, sender)
                decentHolograms != null -> refreshHologramByDH(spawnerName, sender)
                holographicDisplays != null -> refreshHologramByHD(spawnerName, sender)
                else -> sender.sendLang("dependency-not-found")
            }
        }
    }
    dynamic("spawner id", optional = true) {
        suggest {
            Utils.getConfigKeys(config, "hologramText")
        }
        execute<CommandSender> { sender, context, _ ->
            val spawnerName = context["spawner id"]
            when {
                //adyeshach != null -> refreshHologramByADY(spawnerName, sender)
                decentHolograms != null -> refreshHologramByDH(spawnerName, sender)
                holographicDisplays != null -> refreshHologramByHD(spawnerName, sender)
                else -> sender.sendLang("dependency-not-found")
            }
        }
    }
}