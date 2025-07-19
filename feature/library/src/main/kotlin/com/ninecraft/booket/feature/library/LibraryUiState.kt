package com.ninecraft.booket.feature.library

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class LibraryUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val email: String = "",
    val sideEffect: LibrarySideEffect? = null,
    val eventSink: (LibraryUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : LibrarySideEffect
}

sealed interface LibraryUiEvent : CircuitUiEvent {
    data object InitSideEffect : LibraryUiEvent
    data object OnSettingsClick : LibraryUiEvent
}
