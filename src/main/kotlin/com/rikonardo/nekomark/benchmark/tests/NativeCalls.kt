package com.rikonardo.nekomark.benchmark.tests

import com.rikonardo.nekomark.Common
import com.rikonardo.nekomark.benchmark.Test
import com.rikonardo.nekomark.benchmark.nativetest.NativeTest

object NativeCalls : Test<NativeCalls.Results> {
    data class Results(
        val callTimeNanos : List<Pair<Long, Long>>
    ) : Test.Results

    override fun perform(): Results {
        val native = Common.tempdir.resolve("jni/build/nekomark-${Common.osId}-${Common.osArch}.${Common.libExtension}")
        System.load(native.canonicalPath)
        val lib = NativeTest()
        return Results(
            callTimeNanos = (1..5).map {
                val callTimeMillis = System.nanoTime()
                val returnedTimeMillis = lib.test()
                val returnTimeMillis = System.nanoTime()
                Pair(returnedTimeMillis - callTimeMillis, returnTimeMillis - returnedTimeMillis)
            }
        )
    }
}
