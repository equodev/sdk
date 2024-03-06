package com.equo.version

import com.equo.env.getenvOrDefault

class Version(version: String) {
    private var major: String
    private var minor: String? = null
    private var patch: String? = null

    init {
        val list = version.split('.')
        major = list[0]
        if (list.size > 1) {
            minor = list[1]
        }
        if (list.size > 2) {
            patch = list[2]
        }
    }

    fun getMajor(): String {
        return getenvOrDefault("MAJOR", major)
    }

    fun removePatch(): String {
        return "$major.$minor"
    }
}
