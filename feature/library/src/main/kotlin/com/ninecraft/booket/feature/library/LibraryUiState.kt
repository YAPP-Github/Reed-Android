package com.ninecraft.booket.feature.library

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class LibraryUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val email: String = "",
    val sideEffect: LibrarySideEffect? = null,
    val eventSink: (LibraryUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySideEffect {
    data class ShowToast(val message: String) : LibrarySideEffect
}

sealed interface LibraryUiEvent : CircuitUiEvent {
    data object InitSideEffect : LibraryUiEvent
    data object OnSettingsClick : LibraryUiEvent
}
