package com.equo.properties

import java.io.File
import java.util.*


fun Properties(file: File): Properties {
    val props = Properties()
    props.load(file.inputStream())
    return props
}

fun Properties.toStringWithoutComments(): String {
    val output = StringBuilder()
    this.entries.forEach {
        output.append("${it.key}=${it.value}\n")
    }
    return output.toString()
}