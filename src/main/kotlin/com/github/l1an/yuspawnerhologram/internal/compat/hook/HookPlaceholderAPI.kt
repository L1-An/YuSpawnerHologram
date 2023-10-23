package com.github.l1an.yuspawnerhologram.internal.compat.hook

import com.github.l1an.yuspawnerhologram.internal.util.MythicHologramUtils.getSpawnerWarmupTextForPAPI
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object HookPlaceholderAPI : PlaceholderExpansion {
    override val identifier : String
        get() = "spawnerholo"

    override fun onPlaceholderRequest(player : Player?, args : String): String {
        val params = args.split("_")
        val spawnerId = params.getOrNull(0)
        return if (spawnerId != null) {
            getSpawnerWarmupTextForPAPI(spawnerId).toString()
        } else {
            "Insert correct param! (spawner id)"
        }
    }

    override fun onPlaceholderRequest(player : OfflinePlayer?, args : String): String {
        val params = args.split("_")
        val spawnerId = params.getOrNull(0)
        return if (spawnerId != null) {
            getSpawnerWarmupTextForPAPI(spawnerId).toString()
        } else {
            "Insert correct param! (spawner id)"
        }
    }
}