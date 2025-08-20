package com.ninecraft.booket.feature.detail.card

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class RecordCardUiState(
    val isLoading: Boolean = false,
    val quote: String = "",
    val bookTitle: String = "",
    val author: String = "",
    val emotionTag: String = "",
    val isCapturing: Boolean = false,
    val isSharing: Boolean = false,
    val sideEffect: RecordCardSideEffect? = null,
    val eventSink: (RecordCardUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface RecordCardSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : RecordCardSideEffect
}

sealed interface RecordCardUiEvent : CircuitUiEvent {
    data object InitSideEffect : RecordCardUiEvent
    data object OnBackClick : RecordCardUiEvent
    data object OnSaveClick : RecordCardUiEvent
    data object OnShareClick : RecordCardUiEvent
    data class CaptureRecordCard(val bitmap: ImageBitmap) : RecordCardUiEvent
    data class SaveRecordCard(val bitmap: ImageBitmap) : RecordCardUiEvent
    data class ShareRecordCard(val bitmap: ImageBitmap) : RecordCardUiEvent
}
