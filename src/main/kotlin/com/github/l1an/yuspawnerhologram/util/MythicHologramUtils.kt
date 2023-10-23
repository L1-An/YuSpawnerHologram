package com.github.l1an.yuspawnerhologram.util

import com.github.l1an.yuspawnerhologram.internal.compat.hook.HookMythicMobs.getSpawnerManager
import com.github.l1an.yuspawnerhologram.internal.config.YuSpawnerHologramConfig.config
import com.github.l1an.yuspawnerhologram.util.TimeUtils.secondToFormat
import org.yaml.snakeyaml.Yaml
import java.io.File

object MythicHologramUtils {
    /**
     * 从 MythicMobs 的配置文件中获取怪物的显示名
     * @param typeName 怪物的索引名
     * @return 怪物的显示名
     * @author L1An
     * @since 2023/10/23
     */
    fun getDisplayNameFromConfigs(typeName : String) : String? {
        val mobsDir = File("plugins/MythicMobs/Mobs")
        val yaml = Yaml()

        if (!mobsDir.exists() || !mobsDir.isDirectory) {
            return null
        }

        // 遍历所有.yml文件
        val configFiles = mobsDir.listFiles { _, name -> name.endsWith(".yml") } ?: emptyArray()
        for (configFile in configFiles) {
            val configData = yaml.load<Map<String, Any>>(configFile.reader())

            if (configData.containsKey(typeName)) {
                val mobData = configData[typeName] as? Map<*, *>
                val displayName = mobData?.get("Display") as? String
                if (displayName != null) {
                    return displayName
                }
            }
        }
        return null
    }

    fun getSpawnerWarmupTextForPAPI(name : String) : String? {
        val spawnerManager = getSpawnerManager(name) ?: return null
        val warmup = spawnerManager.remainingWarmupSeconds
        val second = secondToFormat(config, warmup, "durationFormat")

        val activeMsg : String = config.getString("papiText.${name}.running")!!.replace("&", "§")
        val secondMsg : String = config.getString("papiText.${name}.waiting")!!
            .replace("&", "§")
            .replace("%warmup%", second)

        return if (!spawnerManager.isOnWarmup) {
            activeMsg
        } else {
            secondMsg
        }
    }
}