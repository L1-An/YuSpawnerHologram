package com.github.l1an.yuspawnerhologram.util

import taboolib.module.configuration.Configuration

object Utils {
    /**
     * 获取配置文件里所有键名
     * @param file 配置文件对象
     * @param section 配置文件下的 section
     * @return 返回字符串列表
     * @author L1An
     * @since 2023/10/23
     */
    fun getConfigKeys(file : Configuration, section : String) : List<String> {
        val getSection = file.getConfigurationSection(section) ?: return emptyList()

        // 返回section下的所有键
        return getSection.getKeys(false).toList()
    }
}