package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test
import java.lang.management.ManagementFactory

object Startup : Test<Startup.Results> {
    data class Results(
        var startupBeginTimeMillis: Long = 0,
        val startupFinishTimeMillis: Long,
        val startupMemoryConsumptionBytes: Long,
        val startupMemoryConsumptionPools: List<Pair<String, Long>>
    ) : Test.Results

    val startupFinishTimeMillis = System.currentTimeMillis()
    private val startupMemoryConsumptionBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    private val startupMemoryConsumptionPools = ManagementFactory.getMemoryPoolMXBeans().map { it.name to it.usage.used }

    override fun perform(): Results {
        return Results(
            startupFinishTimeMillis = startupFinishTimeMillis,
            startupMemoryConsumptionBytes = startupMemoryConsumptionBytes,
            startupMemoryConsumptionPools = startupMemoryConsumptionPools
        )
    }
}
