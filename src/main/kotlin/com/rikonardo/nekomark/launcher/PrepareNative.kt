package com.rikonardo.nekomark.launcher

import com.rikonardo.nekomark.Common
import java.io.File
import kotlin.system.exitProcess

fun prepareNative() {
    println("Preparing JNI testing library")
    val native = Common.tempdir.resolve("jni/build/nekomark-${Common.osId}-${Common.osArch}.${Common.libExtension}")
    if (native.exists()) {
        println("Native library already exists, skipping")
        return
    }
    val javaHome = System.getProperty("java.home")
    val include = File(javaHome, "include")
    val includeOs = include.resolve(Common.osId)
    if (!include.exists()) {
        System.err.println("Could not find jdk \"include\" directory (searched at ${include.canonicalPath})")
        exitProcess(1)
    } else if (!includeOs.exists()) {
        System.err.println("Could not find jdk \"include/(os-specific)\" directory (searched at ${includeOs.canonicalPath})")
        exitProcess(1)
    } else {
        println("JDK includes found: ${include.canonicalPath}")
        println("JDK os-specific includes found: ${includeOs.canonicalPath}")
    }
    val src = ::prepareNative::class.java.getResource("/com/rikonardo/nekomark/native/nekomark.go")!!
    val file = Common.tempdir.resolve("jni/src/nekomark.go")
    file.parentFile.mkdirs()
    file.writeText(
        src.readText()
            .replace("{{include}}", include.canonicalPath)
            .replace("{{include_os}}", includeOs.canonicalPath)
    )
    println("Source code ready, compiling...")
    val compile = ProcessBuilder(
        "go", "build",
        "-o", native.canonicalPath,
        "-buildmode=c-shared",
        file.canonicalPath
    ).directory(file.parentFile).start()
    compile.errorStream.bufferedReader().use { bufferedReader ->
        bufferedReader.lines().forEach {
            System.err.println(it)
        }
    }
    compile.waitFor()
    if (compile.exitValue() != 0) {
        System.err.println("Compilation failed")
        exitProcess(1)
    } else {
        println("Compilation successful")
    }
}
