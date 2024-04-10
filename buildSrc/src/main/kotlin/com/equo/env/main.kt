package com.equo.env

import com.equo.logger.Logger
import kotlin.system.exitProcess

fun getenvOrDefault(name: String, default: String = ""): String {
    val provider = System.getenv(name) ?: default
    return provider.ifBlank {
        throw RuntimeException("$name is not present!")
    }
}