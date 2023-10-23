package com.github.l1an.yuspawnerhologram.internal.manager

data class HologramTextContainer(val text : String, val type : HologramType)
enum class HologramType {
    COOLDOWN,
    WARMUP,
    NEUTRAL
}