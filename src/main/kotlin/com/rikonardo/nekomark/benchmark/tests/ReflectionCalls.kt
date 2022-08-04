package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.benchmark.Test

object ReflectionCalls : Test<ReflectionCalls.Results> {
    data class Results(
        val callTimeNanos: List<Pair<Long, Long>>
    ) : Test.Results

    override fun perform(): Results {
        val c = System::class.java
        val m = c.getMethod("nanoTime")
        return Results(
            callTimeNanos = (1..5).map {
                val callTimeMillis = System.nanoTime()
                val returnedTimeMillis = m.invoke(null) as Long
                val returnTimeMillis = System.nanoTime()
                Pair(returnedTimeMillis - callTimeMillis, returnTimeMillis - returnedTimeMillis)
            }
        )
    }
}
