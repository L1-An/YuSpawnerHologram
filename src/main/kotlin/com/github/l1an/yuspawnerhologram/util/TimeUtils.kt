package com.github.l1an.yuspawnerhologram.util

import taboolib.module.configuration.Configuration

object TimeUtils {
    /**
     * 将秒数转换为格式化的时间
     * @param seconds 秒数
     * @param configuration 配置文件对象
     * @param timeFormatKey 配置文件中的键名
     * @return 返回格式化的时间字符串
     * @author L1An
     * @since 2023/10/23
     */
    fun secondToFormat(
        configuration: Configuration,
        seconds : Int,
        timeFormatKey : String
    ) : String {
        val customFormat = configuration.getString(timeFormatKey) ?: "&6%HH%&e时&6%mm%&e分&6%ss%&e秒" // 当配置文件中没有对应键时，使用默认格式
        val javaFormat = customFormat
            .replace("%HH%", "%02d")
            .replace("%mm%", "%02d")
            .replace("%ss%", "%02d")
            .replace("&", "§")

        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val secs = seconds % 60

        return String.format(javaFormat, hours, minutes, secs)
    }
}