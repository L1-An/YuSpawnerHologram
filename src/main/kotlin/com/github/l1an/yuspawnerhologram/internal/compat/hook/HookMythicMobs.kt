package com.github.l1an.yuspawnerhologram.internal.compat.hook

import com.github.l1an.yuspawnerhologram.YuSpawnerHologram.mythicBukkit
import io.lumine.mythic.core.spawning.spawners.MythicSpawner

object HookMythicMobs {
    /**
     * 根据传入的name获取spawner对象
     * @param name spawner名称
     * @return MythicMobs的spawner对象
     * @author L1An
     * @since 2023/10/23
     */
    fun getSpawnerManager(name : String) : MythicSpawner? {
        return mythicBukkit.spawnerManager.getSpawnerByName(name)
    }
}