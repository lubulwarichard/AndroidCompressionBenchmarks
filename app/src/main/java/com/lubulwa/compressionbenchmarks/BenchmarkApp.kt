package com.lubulwa.compressionbenchmarks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BenchmarkApp : Application() {
    init {
        System.setProperty("org.xerial.snappy.usePureJava", "true");
    }
}