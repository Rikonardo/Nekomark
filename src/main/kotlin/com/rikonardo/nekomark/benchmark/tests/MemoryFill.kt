package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test
import kotlin.random.Random
import kotlin.system.measureNanoTime

object MemoryFill : Test<MemoryFill.Results> {
    data class Results(
        val fillTimeNanos: Long
    ) : Test.Results

    override fun perform(): Results {
        val buffer = Random.nextBytes(5242880)
        return Results(
            fillTimeNanos = measureNanoTime {
                val newBuffer = ByteArray(5242880)
                for (i in 0 until 5242880) {
                    newBuffer[i] = buffer[i]
                }
            }
        )
    }
}
