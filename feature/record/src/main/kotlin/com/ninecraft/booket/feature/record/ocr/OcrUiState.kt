package com.ninecraft.booket.feature.record.ocr

import androidx.camera.core.ImageProxy
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class OcrUiState(
    val currentUi: OcrUi = OcrUi.CAMERA,
    val isPermissionDialogVisible: Boolean = false,
    val sentenceList: ImmutableList<String> = emptyList<String>().toPersistentList(),
    val selectedIndices: Set<Int> = emptySet(),
    val isTextDetectionFailed: Boolean = false,
    val isRecaptureDialogVisible: Boolean = false,
    val eventSink: (OcrUiEvent) -> Unit,
) : CircuitUiState

sealed interface OcrUiEvent : CircuitUiEvent {
    data object OnCloseClick : OcrUiEvent
    data object OnShowPermissionDialog : OcrUiEvent
    data object OnHidePermissionDialog : OcrUiEvent
    data class OnFrameReceived(val imageProxy: ImageProxy) : OcrUiEvent
    data object OnCaptureButtonClick : OcrUiEvent
    data object OnReCaptureButtonClick : OcrUiEvent
    data object OnSelectionConfirmed : OcrUiEvent
    data object OnRecaptureDialogConfirmed : OcrUiEvent
    data object OnRecaptureDialogDismissed : OcrUiEvent
    data class OnSentenceSelected(val index: Int) : OcrUiEvent
}

enum class OcrUi {
    CAMERA,
    RESULT,
}
