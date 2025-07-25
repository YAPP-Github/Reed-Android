package com.ninecraft.booket.feature.record.ocr

import androidx.camera.core.ImageProxy
import com.ninecraft.booket.core.ocr.analyzer.LiveTextAnalyzer
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class OcrUiState(
    val currentUi: OcrUi = OcrUi.CAMERA,
    val sentenceList: ImmutableList<String> = emptyList<String>().toPersistentList(),
    val analyzer: LiveTextAnalyzer? = null,
    val eventSink: (OcrUiEvent) -> Unit,
) : CircuitUiState

sealed interface OcrUiEvent : CircuitUiEvent {
    data object OnCloseClick : OcrUiEvent
    data class OnImageCaptured(val imageProxy: ImageProxy) : OcrUiEvent
    data object OnCapture : OcrUiEvent
    data object OnReCapture : OcrUiEvent
}

enum class OcrUi {
    CAMERA,
    RESULT,
}
