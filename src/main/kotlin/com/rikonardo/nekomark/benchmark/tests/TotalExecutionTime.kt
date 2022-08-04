package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test
import java.lang.management.ManagementFactory

object TotalExecutionTime : Test<TotalExecutionTime.Results> {
    data class Results(
        val executionTimeMillis: Long,
        val shutdownMemoryConsumptionBytes: Long,
        val shutdownMemoryConsumptionPools: List<Pair<String, Long>>
    ) : Test.Results

    override fun perform(): Results {
        return Results(
            executionTimeMillis = System.currentTimeMillis() - Startup.startupFinishTimeMillis,
            shutdownMemoryConsumptionBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
            shutdownMemoryConsumptionPools = ManagementFactory.getMemoryPoolMXBeans().map { it.name to it.usage.used }
        )
    }
}
