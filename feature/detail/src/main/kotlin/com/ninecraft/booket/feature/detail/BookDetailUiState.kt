package com.ninecraft.booket.feature.detail

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class BookDetailUiState(
    val eventSink: (BookDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface BookDetailUiEvent : CircuitUiEvent {
    data object OnBackClicked : BookDetailUiEvent
}
