package com.ninecraft.booket.feature.detail.review

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class ReviewDetailUiState(
    val sideEffect: ReviewDetailSideEffect? = null,
    val eventSink: (ReviewDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface ReviewDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : ReviewDetailSideEffect
}

sealed interface ReviewDetailUiEvent : CircuitUiEvent {
    data object InitSideEffect : ReviewDetailUiEvent
    data object OnBackClicked : ReviewDetailUiEvent
}
