package com.ninecraft.booket.feature.detail.record

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.RecordDetailModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class RecordDetailUiState(
    val recordDetailInfo: RecordDetailModel = RecordDetailModel(),
    val sideEffect: RecordDetailSideEffect? = null,
    val eventSink: (RecordDetailUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface RecordDetailSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : RecordDetailSideEffect
}

sealed interface RecordDetailUiEvent : CircuitUiEvent {
    data object OnCloseClicked : RecordDetailUiEvent
}
