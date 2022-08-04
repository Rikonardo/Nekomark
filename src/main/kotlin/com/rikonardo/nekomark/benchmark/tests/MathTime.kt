package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test
import kotlin.math.*
import kotlin.random.Random
import kotlin.system.measureNanoTime

object MathTime : Test<MathTime.Results> {
    data class Results(
        val executionTimeNanos: List<Long>
    ) : Test.Results

    override fun perform(): Results {
        return Results(
            executionTimeNanos = (1..5).map { measureNanoTime { payload() } }
        )
    }

    private fun payload() {
        var a = 0.0
        for (i in 1..1_000_000) {
            a += sin(i.toDouble())
            a -= cos(i.toDouble()).pow(10)
            a *= i % 10
            a /= i % 10
            a *= sqrt(abs(a + 2))
            if (a.isNaN()) a = i.toDouble()
            a = calcPi(a.roundToInt() % 50)
        }
    }

    private fun calcPi(iterations: Int): Double {
        var x: Double
        var y: Double
        var success = 0
        for (i in 0..iterations) {
            x = Random.nextDouble()
            y = Random.nextDouble()
            if (x.pow(2) + y.pow(2) <= 1) {
                success++
            }
        }
        return (4 * success).toDouble() / iterations
    }
}
