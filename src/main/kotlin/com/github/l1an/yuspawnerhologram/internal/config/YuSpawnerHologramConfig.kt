package com.github.l1an.yuspawnerhologram.internal.config

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

object YuSpawnerHologramConfig {
    @Config("config.yml")
    lateinit var config: Configuration
        private set
}