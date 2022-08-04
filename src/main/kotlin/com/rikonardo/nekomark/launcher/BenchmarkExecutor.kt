package com.rikonardo.nekomark.launcher

import com.rikonardo.nekomark.benchmark.Test
import com.rikonardo.nekomark.benchmark.tests.Startup
import java.io.EOFException
import java.io.File
import java.io.ObjectInputStream
import java.lang.management.ManagementFactory
import java.net.URISyntaxException

var currentJar: File? = try {
    File(::currentJar::class.java.protectionDomain.codeSource.location.toURI())
} catch (e: URISyntaxException) {
    null
}

private fun getClassPathArg(): String {
    return if (currentJar == null || currentJar!!.isDirectory) ManagementFactory.getRuntimeMXBean().classPath else currentJar.toString()
}

fun runBenchmark(jvmArgs: List<String>, useNativeTest: Boolean): List<Test.Results> {
    val javaHome = System.getProperty("java.home")
    val javaExecutable = File(javaHome, "bin/java").let { if (it.exists()) it else File(javaHome, "bin/java.exe") }
    val startTime = System.currentTimeMillis()
    val run = ProcessBuilder(
        javaExecutable.canonicalPath, *jvmArgs.toTypedArray(),
        "-cp", getClassPathArg(),
        "com.rikonardo.nekomark.benchmark.BenchmarkMainKt",
        *(if (useNativeTest) arrayOf("--use-native-test") else arrayOf())
    ).start()
    val input = ObjectInputStream(run.inputStream)
    val results = mutableListOf<Test.Results>()
    while (true) {
        val obj = try { input.readObject() ?: break } catch (e: EOFException) { break }
        if (obj is Startup.Results) obj.startupBeginTimeMillis = startTime
        results.add(obj as Test.Results)
    }
    run.waitFor()
    return results
}
