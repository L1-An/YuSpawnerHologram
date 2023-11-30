package com.github.l1an.yuspawnerhologram.internal.command

import com.github.l1an.yuspawnerhologram.internal.command.subcommand.CommandRefresh
import com.github.l1an.yuspawnerhologram.internal.command.subcommand.CommandReload
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.common5.Mirror
import taboolib.common5.mirrorNow
import taboolib.expansion.createHelper

@CommandHeader(name = "yuspawnerhologram", aliases = ["spawnerholo", "mmholo"])
object MainCommand {
    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reload = CommandReload

    @CommandBody
    val refresh = CommandRefresh

    @CommandBody
    val report = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Mirror.report(sender)
        }
    }
}