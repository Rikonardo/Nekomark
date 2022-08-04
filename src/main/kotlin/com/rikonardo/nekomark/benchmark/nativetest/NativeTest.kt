package com.rikonardo.nekomark.benchmark.nativetest

class NativeTest {
    external fun test(): Long
    fun nanos(): Long = System.nanoTime()
}
