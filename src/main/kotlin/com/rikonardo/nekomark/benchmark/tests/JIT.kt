package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test
import kotlin.math.*
import kotlin.system.measureNanoTime

object JIT : Test<JIT.Results> {
    data class Results(
        val compileTimeNanos: Long
    ): Test.Results

    override fun perform(): Results {
        val first = measureNanoTime { payload() }
        val second = measureNanoTime { payload() }
        return Results(
            compileTimeNanos = max(first - second, 0)
        )
    }

    private fun payload() {
        var x = 0.0
        var y = 42.0
        for (i in 0 until 10) {
            x += if (i % 2 == 0) {
                cos(y)
            } else {
                sin(y)
            }
            y -= x.pow(i + 4)
        }
        x += y.pow(2)
        y /= x + 10
        for (char in "1234567890") {
            x += char.code
            y -= char.toString().toInt()
        }
        x = Double.MAX_VALUE
        if (x == y) {
            payload()
        }
        x = y
        y += (x.roundToInt() shr 1)
        Any().hashCode()
        Class.forName("java.lang.String")
        Math.PI
        Runtime.getRuntime().availableProcessors()
    }
}
