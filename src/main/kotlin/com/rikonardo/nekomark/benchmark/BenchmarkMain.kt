package com.rikonardo.nekomark.benchmark

import com.rikonardo.nekomark.benchmark.tests.*
import java.io.ObjectOutputStream

val tests = mutableSetOf(
    Startup,
    JIT,
    MathTime,
    NativeCalls,
    ReflectionCalls,
    MemoryFill,
    TotalExecutionTime
)

val out = ObjectOutputStream(System.out)

fun main(args: Array<String>) {
    if (!args.contains("--use-native-test")) tests.remove(NativeCalls)
    tests.forEach {
        out.writeObject(it.perform())
        out.flush()
    }
    out.close()
}
