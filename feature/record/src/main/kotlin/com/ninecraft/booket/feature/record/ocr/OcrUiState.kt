package com.ninecraft.booket.feature.record.ocr

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class OcrUiState(
    val currentUi: OcrUi = OcrUi.CAMERA,
    val sentenceList: ImmutableList<String> = emptyList<String>().toPersistentList(),
    val eventSink: (OcrUiEvent) -> Unit,
) : CircuitUiState

sealed interface OcrUiEvent : CircuitUiEvent {
    data object OnCloseClick : OcrUiEvent
    data object OnCapture : OcrUiEvent
    data object OnReCapture : OcrUiEvent
}

enum class OcrUi {
    CAMERA,
    RESULT,
}
