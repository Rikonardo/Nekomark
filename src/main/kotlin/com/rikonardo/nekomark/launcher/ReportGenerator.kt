package com.rikonardo.nekomark.launcher

import com.rikonardo.nekomark.Common
import com.rikonardo.nekomark.benchmark.Test
import com.rikonardo.nekomark.benchmark.tests.*

@Suppress("DuplicatedCode")
fun generateReport(iterations: List<List<Test.Results>>): String {
    var text = ""
    fun append(s: String = "") {
        text += s + "\n"
    }
    append("Nekomark v${Common.version} report")
    append("-".repeat(32))
    append(
        (System.getProperty("java.vendor.version") ?: System.getProperty("java.vm.name")
        ?: System.getProperty("java.runtime.name") ?: "Unknown")
    )
    append(
        "Java version: " + (System.getProperty("java.version") ?: System.getProperty("java.runtime.version")
        ?: System.getProperty("java.vm.version") ?: "Unknown")
    )
    append("-".repeat(32))

    @Suppress("UNCHECKED_CAST")
    fun <T : Test.Results> allDouble(
        index: Int,
        units: String = "",
        decimal: Int = 2,
        decimalAvg: Int = 2,
        resolver: (T) -> Number
    ): String {
        val unitsPlaceholder = if (units.isNotEmpty()) " $units" else ""
        val values = iterations.map { resolver(it[index] as T).toDouble() }
        val avg = values.average()
        return values.joinToString(", ") { "%.${decimal}f".format(it) + unitsPlaceholder } +
                if (iterations.size > 1) " (avg: ${"%.${decimalAvg}f".format(avg)}$unitsPlaceholder)" else ""
    }

    iterations.first().forEachIndexed { i, base ->
        append()
        when (base) {
            is Startup.Results -> {
                append("Startup time: " + allDouble<Startup.Results>(i, "ms", 0) {
                    it.startupFinishTimeMillis - it.startupBeginTimeMillis
                })
                append()
                append("Total RAM consumption at start: " + allDouble<Startup.Results>(i, "MB") {
                    it.startupMemoryConsumptionBytes / 1024.0 / 1024.0
                })
                append()
                append("RAM consumption pools at start:")
                val longestName = base.startupMemoryConsumptionPools.maxOfOrNull { it.first.length } ?: 0
                base.startupMemoryConsumptionPools.forEachIndexed { pi, pool ->
                    append("  ${pool.first}:${" ".repeat(longestName - pool.first.length)} " +
                            allDouble<Startup.Results>(i, "MB") {
                                it.startupMemoryConsumptionPools[pi].second / 1024.0 / 1024.0
                            }
                    )
                }
            }
            is MathTime.Results -> {
                append("Execution time for heavy math method:")
                base.executionTimeNanos.indices.forEach { ind ->
                    append("  Call ${ind + 1}: " + allDouble<MathTime.Results>(i, "ms") {
                        it.executionTimeNanos[ind] / 1_000_000.0
                    })
                }
            }
            is MemoryFill.Results -> {
                append("Time to copy 5MB of data in RAM: " + allDouble<MemoryFill.Results>(i, "ms") {
                    it.fillTimeNanos / 1_000_000.0
                })
            }
            is JIT.Results -> {
                append("JIT compilation overhead: " + allDouble<JIT.Results>(i, "ms", 6, 6) {
                    it.compileTimeNanos / 1_000_000.0
                })
            }
            is NativeCalls.Results -> {
                append("Time spent for JNI method call:")
                base.callTimeNanos.indices.forEach { ind ->
                    append("  Call ${ind + 1}:")
                    append("    Call time:   " + allDouble<NativeCalls.Results>(i, "ms", 6, 6) {
                        it.callTimeNanos[ind].first / 1_000_000.0
                    })
                    append("    Return time: " + allDouble<NativeCalls.Results>(i, "ms", 6, 6) {
                        it.callTimeNanos[ind].second / 1_000_000.0
                    })
                    append("    Total:       " + allDouble<NativeCalls.Results>(i, "ms", 6, 6) {
                        (it.callTimeNanos[ind].first + it.callTimeNanos[ind].second) / 1_000_000.0
                    })
                }
            }
            is ReflectionCalls.Results -> {
                append("Time spent for reflection method call:")
                base.callTimeNanos.indices.forEach { ind ->
                    append("  Call ${ind + 1}:")
                    append("    Call time:   " + allDouble<ReflectionCalls.Results>(i, "ms", 6, 6) {
                        it.callTimeNanos[ind].first / 1_000_000.0
                    })
                    append("    Return time: " + allDouble<ReflectionCalls.Results>(i, "ms", 6, 6) {
                        it.callTimeNanos[ind].second / 1_000_000.0
                    })
                    append("    Total:       " + allDouble<ReflectionCalls.Results>(i, "ms", 6, 6) {
                        (it.callTimeNanos[ind].first + it.callTimeNanos[ind].second) / 1_000_000.0
                    })
                }
            }
            is TotalExecutionTime.Results -> {
                append("Total execution time: " + allDouble<TotalExecutionTime.Results>(i, "ms", 0) {
                    it.executionTimeMillis
                })
                append()
                append("Total RAM consumption at finish: " + allDouble<TotalExecutionTime.Results>(i, "MB") {
                    it.shutdownMemoryConsumptionBytes / 1024.0 / 1024.0
                })
                append()
                append("RAM consumption pools at finish:")
                val longestName = base.shutdownMemoryConsumptionPools.maxOfOrNull { it.first.length } ?: 0
                base.shutdownMemoryConsumptionPools.forEachIndexed { pi, pool ->
                    append("  ${pool.first}:${" ".repeat(longestName - pool.first.length)} " +
                            allDouble<TotalExecutionTime.Results>(i, "MB") {
                                it.shutdownMemoryConsumptionPools[pi].second / 1024.0 / 1024.0
                            }
                    )
                }
            }
        }
    }

    return text
}
