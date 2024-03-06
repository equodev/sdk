package com.equo.gsutil

import com.equo.env.getenvOrDefault
import com.equo.logger.Logger
import com.equo.string.runCommand
import java.util.HashMap

class GSUtil {
    private val gcloudHome: String = getenvOrDefault("GOOGLE_CLOUD_HOME", "/google-cloud-sdk")
    private val gsutil = "$gcloudHome/bin/gsutil"
    private val headers = HashMap<String, String>()

    private fun flatMapHeaders(): String {
        var result = ""
        headers.entries.forEach {
            result += """-h "${it.key}":" ${it.value}" """
        }
        return result
    }

    private fun run(command: String): String {
        val command_ = "$gsutil ${flatMapHeaders()} $command"
        Logger.code(command_)
        return command_.runCommand() ?: ""
    }

    fun addHeader(key: String, value: String): GSUtil {
        headers[key] = value
        return this
    }

    fun setCORS(corsJsonFile: String, bucketName: String) {
        run("cors set $corsJsonFile gs://$bucketName")
    }

    fun makeBucket(bucketName: String): String {
        // mb - Make buckets
        return run("mb gs://$bucketName")
    }

    fun cat(path: String): String {
        // cat - Concatenate object content to stdout
        return run("cat gs://$path")
    }

    fun copy(src_url: String, dst_url: String, options: String = ""): String {
        // cp - Copy files and objects
        // The ``-R`` and ``-r`` options are synonymous. They enable directories,
        // buckets, and bucket subdirectories to be copied recursively.
        return run("cp $options $src_url $dst_url")
    }
}