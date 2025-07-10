package com.ninecraft.booket.feature.search

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SearchUiState(
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface SearchUiEvent : CircuitUiEvent
