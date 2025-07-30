package com.ninecraft.booket.feature.main.splash

import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val idle: Boolean = true,
    val isOnboardingCompleted: Boolean? = null,
) : CircuitUiState
