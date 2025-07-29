package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.text.input.TextFieldState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

data class LibrarySearchUiState(
    val uiState: UiState = UiState.Idle,
    val queryState: TextFieldState = TextFieldState(),
    val eventSink: (LibrarySearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySearchUiEvent : CircuitUiEvent {
    data class OnSearchClick(val query: String) : LibrarySearchUiEvent
    data object OnClearClick : LibrarySearchUiEvent
    data object OnBackClick : LibrarySearchUiEvent
}
