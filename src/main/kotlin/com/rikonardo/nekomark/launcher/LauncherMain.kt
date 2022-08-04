package com.rikonardo.nekomark.launcher

import com.rikonardo.nekomark.Common
import com.rikonardo.nekomark.benchmark.Test
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val parser = ArgParser("java -jar Nekomark.jar")
    val iterations by parser.option(ArgType.Int, fullName = "iterations", description = "Test iterations count")
        .default(3)
    val nativeTest by parser.option(
        ArgType.Boolean,
        fullName = "enable-jni-test",
        description = "Enable JNI test (requires GO compiler)"
    ).default(false)
    val jvmArgs by parser.option(ArgType.String, fullName = "jvm-args", description = "JVM arguments")
        .default("-Xms512m -Xmx512m")
    val output by parser.option(ArgType.String, fullName = "output", description = "Report output file")
        .default("nekomark-report.txt")
    parser.parse(args)

    if (iterations < 1) {
        System.err.println("Iterations count must be greater than 0")
        exitProcess(1)
    }

    println("Nekomark v${Common.version} - https://github.com/Rikonardo/Nekomark")
    println(
        "Benchmarking " + (System.getProperty("java.vendor.version") ?: System.getProperty("java.vm.name")
        ?: System.getProperty("java.runtime.name") ?: "Unknown")
    )
    println()

    if (nativeTest) prepareNative()

    val resultSets = mutableListOf<List<Test.Results>>()
    for (i in 1..iterations) {
        println("Iteration $i...")
        val time = measureTimeMillis {
            resultSets.add(runBenchmark(jvmArgs.split(" "), nativeTest))
        }
        println("Iteration $i finished in $time ms")
    }
    println("Test finished, writing report to $output")
    val report = generateReport(resultSets)
    File(output).writeText(report)
}
