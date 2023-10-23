package com.github.l1an.yuspawnerhologram.internal.core

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.getHologramTextWithInfo
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.holograms
import com.github.l1an.yuspawnerhologram.util.MythicHologramUtils.getDisplayNameFromConfigs
import com.github.l1an.yuspawnerhologram.util.TimeUtils.secondToFormat
import com.github.l1an.yuspawnerhologram.internal.manager.HologramType.*
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit
import taboolib.common5.Coerce
import taboolib.common5.mirrorNow

object HologramUpdateSubmit {
    @Awake(LifeCycle.ENABLE)
    fun hologramUpdateTime() {
        submit(delay = 40, period = 20) {
            mirrorNow("Refresh HologramText") {
                for ((name, hologram) in holograms.entries) {
                    // 获取刷怪管理器
                    val spawnerManager = getSpawnerManager(name)!!

                    // 获取所有行
                    val lines = hologram.lines
                    for (i in 0 until lines.size()) {
                        val line = lines[i]
                        if (line is TextHologramLine) {
                            // 获取 hologram 的每行内容
                            val hologramInfo = getHologramTextWithInfo(
                                config,
                                "hologramText",
                                name,
                                getDisplayNameFromConfigs(spawnerManager.typeName)!!,
                                spawnerManager.remainingCooldownSeconds,
                                spawnerManager.remainingWarmupSeconds
                            )[i]

                            when (hologramInfo.type) {
                                COOLDOWN -> {
                                    line.text = hologramInfo.text.replace("%cooldown%", secondToFormat(config, spawnerManager.remainingCooldownSeconds, "durationFormat"))
                                }
                                WARMUP -> {
                                    line.text = if (!spawnerManager.isOnWarmup) {
                                        activeMsg
                                    } else {
                                        hologramInfo.text.replace("%warmup%", secondToFormat(config, spawnerManager.remainingWarmupSeconds, "durationFormat"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val activeMsg : String
        get() = Coerce.toString(config["activeMsg"]).replace("&", "§") ?: "§a激活中"
}