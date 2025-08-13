package com.ninecraft.booket.feature.detail.record

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.RecordDetailModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val exception: Throwable) : UiState
}

data class RecordDetailUiState(
    val uiState: UiState = UiState.Idle,
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
    data object onRetryClick : RecordDetailUiEvent
}
