package com.ninecraft.booket.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ninecraft.booket.core.common.extensions.openPlayStore
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
                context.openPlayStore()
            }
            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(SplashUiEvent.InitSideEffect)
        }
    }
}
