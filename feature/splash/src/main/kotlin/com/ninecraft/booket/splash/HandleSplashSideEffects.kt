package com.ninecraft.booket.splash

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.ninecraft.booket.feature.splash.BuildConfig
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleSplashSideEffects(
    state: SplashUiState,
    eventSink: (SplashUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is SplashSideEffect.NavigateToPlayStore -> {
                openPlayStore(context)
            }
            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(SplashUiEvent.InitSideEffect)
        }
    }
}

private fun openPlayStore(context: Context) {
    // https://play.google.com/store/apps/details?id=com.ninecraft.booket
    val intent = Intent(Intent.ACTION_VIEW, "market://details?id=${BuildConfig.PACKAGE_NAME}".toUri())
    context.startActivity(intent)
}
