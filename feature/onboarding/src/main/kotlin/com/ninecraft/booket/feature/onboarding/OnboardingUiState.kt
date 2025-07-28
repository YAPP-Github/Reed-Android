package com.ninecraft.booket.feature.onboarding

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class OnboardingUiState(
    val eventSink: (OnboardingUiEvent) -> Unit,
) : CircuitUiState

sealed interface OnboardingUiEvent : CircuitUiEvent {
    data object OnNextButtonClick : OnboardingUiEvent
}
