package com.ninecraft.booket.feature.main.splash

import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.model.OnboardingState
import com.slack.circuit.runtime.CircuitUiState

data class SplashUiState(
    val idle: Boolean = true,
    val onboardingState: OnboardingState = OnboardingState.IDLE,
    val autoLoginState: AutoLoginState = AutoLoginState.IDLE,
) : CircuitUiState
