package com.ninecraft.booket.feature.detail.book

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class BookDetailUiState(
    val sideEffect: BookDetailSideEffect? = null,
    val eventSink: (BookDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface BookDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : BookDetailSideEffect
}

sealed interface BookDetailUiEvent : CircuitUiEvent {
    data object InitSideEffect : BookDetailUiEvent
    data object OnBackClicked : BookDetailUiEvent
    data object OnBeforeReadingClick : BookDetailUiEvent
    data object OnReadingClick : BookDetailUiEvent
    data object OnCompletedClick : BookDetailUiEvent
}
