package com.github.l1an.yuspawnerhologram.internal.command.subcommand

import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.refreshHologram
import com.github.l1an.yuspawnerhologram.util.Utils
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest

val CommandRefresh = subCommand {
    execute<CommandSender> { sender, _, _ ->
        val keys = Utils.getConfigKeys(config, "hologramText")
        for (spawnerName in keys) {
            refreshHologram(spawnerName, sender)
        }
    }
    dynamic("spawner id", optional = true) {
        suggest {
            Utils.getConfigKeys(config, "hologramText")
        }
        execute<CommandSender> { sender, context, _ ->
            refreshHologram(context["spawner id"], sender)
        }
    }
}