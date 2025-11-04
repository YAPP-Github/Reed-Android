package com.ninecraft.booket.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ninecraft.booket.BuildConfig
import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer

class ComposeStabilityAnalyzerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ComposeStabilityAnalyzer.setEnabled(BuildConfig.DEBUG)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
