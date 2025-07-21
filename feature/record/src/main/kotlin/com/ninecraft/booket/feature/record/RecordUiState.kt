package com.ninecraft.booket.feature.record

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class RecordUiState(
    val isLoading: Boolean = false,
    val eventSink: (RecordUiEvent) -> Unit,
) : CircuitUiState

sealed interface RecordUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : RecordUiEvent
}
