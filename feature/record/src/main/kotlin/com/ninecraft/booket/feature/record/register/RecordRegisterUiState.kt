package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.model.Emotion
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import java.util.UUID

data class RecordRegisterUiState(
    val isLoading: Boolean = false,
    val currentStep: RecordStep = RecordStep.QUOTE,
    val recordPageState: TextFieldState = TextFieldState(),
    val recordSentenceState: TextFieldState = TextFieldState(),
    val isPageError: Boolean = false,
    val memoState: TextFieldState = TextFieldState(),
    val emotions: ImmutableList<Emotion> = persistentListOf(),
    val emotionDetails: ImmutableList<String> = persistentListOf(),
    val selectedEmotion: Emotion? = null,
    val selectedEmotionDetails: PersistentMap<Emotion, ImmutableList<String>> = persistentMapOf(),
    val committedEmotion: Emotion? = null,
    val committedEmotionDetails: PersistentMap<Emotion, ImmutableList<String>> = persistentMapOf(),
    val isEmotionDetailBottomSheetVisible: Boolean = false,
    val impressionState: TextFieldState = TextFieldState(),
    val impressionGuideList: ImmutableList<String> = persistentListOf(),
    val selectedImpressionGuide: String = "",
    val beforeSelectedImpressionGuide: String = "",
    val savedRecordId: String = "",
    val isNextButtonEnabled: Boolean = false,
    val isImpressionGuideBottomSheetVisible: Boolean = false,
    val isExitDialogVisible: Boolean = false,
    val isRecordSavedDialogVisible: Boolean = false,
    val isScanTooltipVisible: Boolean = true,
    val isImpressionGuideTooltipVisible: Boolean = true,
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
    data class OnSelectEmotion(val emotion: Emotion) : RecordRegisterUiEvent
    data class OnSelectEmotionV2(val emotion: Emotion) : RecordRegisterUiEvent
    data class OnEmotionDetailToggled(val detail: String) : RecordRegisterUiEvent
    data class OnEmotionDetailRemoved(val detail: String) : RecordRegisterUiEvent
    data object OnEmotionDetailSkipped : RecordRegisterUiEvent
    data object OnEmotionDetailCommitted : RecordRegisterUiEvent
    data object OnEmotionDetailBottomSheetDismiss : RecordRegisterUiEvent
    data object OnImpressionGuideButtonClick : RecordRegisterUiEvent
    data object OnImpressionGuideBottomSheetDismiss : RecordRegisterUiEvent
    data class OnSelectImpressionGuide(val index: Int) : RecordRegisterUiEvent
    data object OnImpressionGuideConfirmed : RecordRegisterUiEvent
    data object OnExitDialogConfirm : RecordRegisterUiEvent
    data object OnExitDialogDismiss : RecordRegisterUiEvent
    data class OnRecordSavedDialogConfirm(val recordId: String) : RecordRegisterUiEvent
    data object OnRecordSavedDialogDismiss : RecordRegisterUiEvent
}
