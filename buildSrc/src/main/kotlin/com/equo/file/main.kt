package com.equo.file

import com.equo.logger.Logger
import java.io.File
import org.gradle.api.Project

fun writeNewFile(project: Project, filename: String, content: ByteArray, printContent: Boolean = true): File {
    val file = project.file(filename)
    if (!file.exists()) {
        file.createNewFile()
    }
    file.writeBytes(content)
    Logger.info("File «${file.absolutePath}» created successfully!")
    if (printContent) {
        Logger.info("### BEGIN Content ###")
        Logger.info(String(content))
        Logger.info("### END Content ###")
    }
    return file
}
