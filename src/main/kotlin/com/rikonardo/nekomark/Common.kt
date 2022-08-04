package com.rikonardo.nekomark

import java.io.File
import java.util.*

object Common {
    enum class OS { WINDOWS, LINUX, MAC }

    val version: String = try {
        val p = Properties()
        p.load(javaClass.getResourceAsStream("/com/rikonardo/nekomark/build.properties"))
        p.getProperty("version")
    } catch (e: Exception) {
        "[unknown]"
    }

    val tempdir = File(".nekomark").also { it.mkdir() }
    private val osName = System.getProperty("os.name")
    val osArch = System.getProperty("os.arch")
    val os = osName.lowercase().let {
        if (it.contains("win"))
            OS.WINDOWS
        else if (it.contains("nix") || it.contains("nux") || it.contains("aix"))
            OS.LINUX
        else if (it.contains("mac"))
            OS.MAC
        else throw IllegalArgumentException("Unsupported OS: $osName")
    }
    val libExtension = when (os) {
        OS.WINDOWS -> "dll"
        OS.LINUX -> "so"
        OS.MAC -> "dylib"
    }
    val osId = when (os) {
        OS.WINDOWS -> "win32"
        OS.LINUX -> "linux"
        OS.MAC -> "darwin"
    }
}
