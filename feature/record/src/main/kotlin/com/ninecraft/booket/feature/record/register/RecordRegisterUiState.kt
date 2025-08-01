package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.RecordStep
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class RecordRegisterUiState(
    val currentStep: RecordStep = RecordStep.QUOTE,
    val recordPageState: TextFieldState = TextFieldState(),
    val recordSentenceState: TextFieldState = TextFieldState(),
    val isPageError: Boolean = false,
    val emotionTags: ImmutableList<EmotionTag> = persistentListOf(),
    val selectedEmotion: EmotionTag? = null,
    val impressionState: TextFieldState = TextFieldState(),
    val impressionGuideList: ImmutableList<String> = persistentListOf(),
    val selectedImpressionGuide: String = "",
    val beforeSelectedImpressionGuide: String = "",
    val isNextButtonEnabled: Boolean = false,
    val isImpressionGuideBottomSheetVisible: Boolean = false,
    val isExitDialogVisible: Boolean = false,
    val isRecordSavedDialogVisible: Boolean = false,
    val sideEffect: RecordRegisterSideEffect? = null,
    val eventSink: (RecordRegisterUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface RecordRegisterSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : RecordRegisterSideEffect
}

sealed interface RecordRegisterUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : RecordRegisterUiEvent
    data object OnClearClick : RecordRegisterUiEvent
    data object OnNextButtonClick : RecordRegisterUiEvent
    data object OnSentenceScanButtonClick : RecordRegisterUiEvent
    data class OnSelectEmotion(val emotion: EmotionTag) : RecordRegisterUiEvent
    data object OnImpressionGuideButtonClick : RecordRegisterUiEvent
    data object OnImpressionGuideBottomSheetDismiss : RecordRegisterUiEvent
    data class OnSelectImpressionGuide(val index: Int) : RecordRegisterUiEvent
    data object OnImpressionGuideConfirmed : RecordRegisterUiEvent
    data object OnExitDialogConfirm : RecordRegisterUiEvent
    data object OnExitDialogDismiss : RecordRegisterUiEvent
    data class OnRecordSavedDialogConfirm(val recordId: String) : RecordRegisterUiEvent
    data object OnRecordSavedDialogDismiss : RecordRegisterUiEvent
}
