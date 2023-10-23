package com.github.l1an.yuspawnerhologram.internal.manager

import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig

object ConfigManager {

    fun reload() {
        YuSpawnerHologramConfig.config.reload()
    }
}