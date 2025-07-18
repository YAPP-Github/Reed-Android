package com.ninecraft.booket.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data object OnButtonClick: HomeUiEvent
}
