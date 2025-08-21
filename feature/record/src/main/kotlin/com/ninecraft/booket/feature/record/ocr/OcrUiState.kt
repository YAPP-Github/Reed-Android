package com.ninecraft.booket.feature.record.ocr

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class OcrUiState(
    val currentUi: OcrUi = OcrUi.CAMERA,
    val isPermissionDialogVisible: Boolean = false,
    val sentenceList: ImmutableList<String> = persistentListOf(),
    val selectedIndices: Set<Int> = emptySet(),
    val isTextDetectionFailed: Boolean = false,
    val isRecaptureDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val sideEffect: OcrSideEffect? = null,
    val eventSink: (OcrUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface OcrSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : OcrSideEffect
}

sealed interface OcrUiEvent : CircuitUiEvent {
    data object OnCloseClick : OcrUiEvent
    data object OnShowPermissionDialog : OcrUiEvent
    data object OnHidePermissionDialog : OcrUiEvent
    data class OnCaptureButtonClick(val imageUri: Uri) : OcrUiEvent
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
