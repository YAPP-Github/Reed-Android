package com.ninecraft.booket.feature.detail.record

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class RecordDetailUiState(
    val sideEffect: RecordDetailSideEffect? = null,
    val eventSink: (RecordDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface RecordDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : RecordDetailSideEffect
}

sealed interface RecordDetailUiEvent : CircuitUiEvent {
    data object OnCloseClicked : RecordDetailUiEvent
}
