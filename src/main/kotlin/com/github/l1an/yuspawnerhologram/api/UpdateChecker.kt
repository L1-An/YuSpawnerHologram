package com.github.l1an.yuspawnerhologram.api

import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
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
                info("Unable to check for updates: " + e.message)
            }
        }
    }
}