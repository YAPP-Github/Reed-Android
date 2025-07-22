package com.ninecraft.booket.feature.record.register

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class RecordUiState(
    val isLoading: Boolean = false,
    val eventSink: (RecordRegisterUiEvent) -> Unit,
) : CircuitUiState

sealed interface RecordRegisterUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : RecordRegisterUiEvent
    data object OnNextButtonClick : RecordRegisterUiEvent
}
