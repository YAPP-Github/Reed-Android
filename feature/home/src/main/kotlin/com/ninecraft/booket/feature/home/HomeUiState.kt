package com.ninecraft.booket.feature.home

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class HomeUiState(
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data object OnSettingsClick : HomeUiEvent
    data object OnBookRegisterClick : HomeUiEvent
    data object OnRecordButtonClick : HomeUiEvent
    data object OnBookDetailClick : HomeUiEvent
}

data class Book(
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val imageUrl: String = "",
    val reviewCount: Int = 0,
)
