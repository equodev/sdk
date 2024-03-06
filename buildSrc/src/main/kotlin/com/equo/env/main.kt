package com.equo.env

import com.equo.logger.Logger
import kotlin.system.exitProcess

fun getenvOrDefault(name: String, default: String = ""): String {
    val provider = System.getenv(name) ?: default
    return provider.ifBlank {
        Logger.error("$name is not present!")
        exitProcess(1)
    }
}