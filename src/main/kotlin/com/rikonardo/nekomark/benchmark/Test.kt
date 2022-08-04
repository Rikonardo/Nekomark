package com.rikonardo.nekomark.benchmark

interface Test<R : Test.Results> {
    fun perform(): R
    interface Results: java.io.Serializable
}
