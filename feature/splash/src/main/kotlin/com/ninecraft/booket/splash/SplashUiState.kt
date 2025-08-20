package com.ninecraft.booket.splash

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val isForceUpdateDialogVisible: Boolean = false,
    val sideEffect: SplashSideEffect? = null,
    val eventSink: (SplashUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface SplashSideEffect {
    data object NavigateToPlayStore : SplashSideEffect
}

sealed interface SplashUiEvent : CircuitUiEvent {
    data object InitSideEffect : SplashUiEvent
    data object OnUpdateButtonClick : SplashUiEvent
}
