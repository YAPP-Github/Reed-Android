package com.ninecraft.booket.feature.search.library

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class LibrarySearchUiState(
    val eventSink: (LibrarySearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySearchUiEvent : CircuitUiEvent {
    data object OnBackClick : LibrarySearchUiEvent
}
