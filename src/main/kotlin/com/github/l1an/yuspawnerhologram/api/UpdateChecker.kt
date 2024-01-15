package com.github.l1an.yuspawnerhologram.api

import com.github.l1an.yuspawnerhologram.YuSpawnerHologram.messagePrefix
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.function.Consumer

class UpdateChecker(private val resourceId: Int) {
    fun getVersion(consumer: Consumer<String?>) {
        submit(async = true) {
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId + "/~")
                    .openStream().use { `is` ->
                        Scanner(`is`).use { scann ->
                            if (scann.hasNext()) {
                                consumer.accept(scann.next())
                            }
                        }
                    }
            } catch (e: IOException) {
                console().sendMessage("$messagePrefix &cUnable to check for update. This may be a problem caused by the network.".colored())
            }
        }
    }
}