package com.github.l1an.yuspawnerhologram.internal.core

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.AdyeshachHologram.refreshHologramTextByADY
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.DecentHologram.refreshHologramByDH
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.DecentHologram.refreshHologramTextByDH
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.adyeshach
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.decentHolograms
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HologramEnter.holographicDisplays
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.getHologramTextWithInfo
import com.github.l1an.yuspawnerhologram.internal.core.mythichologram.HolographicHologram.holograms
import com.github.l1an.yuspawnerhologram.internal.util.MythicHologramUtils.getDisplayNameFromConfigs
import com.github.l1an.yuspawnerhologram.util.TimeUtils.secondToFormat
import com.github.l1an.yuspawnerhologram.internal.manager.HologramType.*
import com.github.l1an.yuspawnerhologram.util.Utils.getConfigKeys
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit
import taboolib.common5.Coerce
import taboolib.common5.mirrorNow

object HologramUpdateSubmit {
    @Awake(LifeCycle.ENABLE)
    fun hologramUpdateSubmitEnter() {
        when {
            adyeshach != null -> hologramUpdateTimeForADY()
            decentHolograms != null -> hologramUpdateTimeForDH()
            holographicDisplays != null -> hologramUpdateTimeForHD()
        }
    }

    private fun hologramUpdateTimeForHD() {
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
                                name,
                                getDisplayNameFromConfigs(spawnerManager.typeName)!!,
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
                                NEUTRAL -> {
                                    line.text = hologramInfo.text
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hologramUpdateTimeForDH() {
        submit(delay = 40, period = 20) {
            mirrorNow("Refresh HologramText") {
                for (spawnerName in getConfigKeys(config, "hologramText")) {
                    refreshHologramTextByDH(spawnerName)
                }
            }
        }
    }

    private fun hologramUpdateTimeForADY() {
        submit(delay = 40, period = 20) {
            mirrorNow("Refresh HologramText") {
                for (spawnerName in getConfigKeys(config, "hologramText")) {
                    refreshHologramTextByADY(spawnerName)
                }
            }
        }
    }

    val activeMsg : String
        get() = Coerce.toString(config["activeMsg"]).replace("&", "§") ?: "§a激活中"
}