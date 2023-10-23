package com.github.l1an.yuspawnerhologram.internal.command.subcommand

import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.isDebug
import com.github.l1an.yuspawnerhologram.internal.manager.ConfigManager
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common5.mirrorNow
import taboolib.platform.util.sendLang

val CommandReload = subCommand {
    execute<CommandSender> {sender, _, _ ->
        mirrorNow("Plugin Reload") {
            sender.sendLang("command-reload")
            ConfigManager.reload()
            if (isDebug) {
                sender.sendLang("debug-mode")
            }
        }
    }
}