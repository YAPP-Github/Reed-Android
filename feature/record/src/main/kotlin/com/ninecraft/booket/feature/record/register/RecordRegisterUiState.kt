package com.ninecraft.booket.feature.record.register

import androidx.compose.foundation.text.input.TextFieldState
import com.ninecraft.booket.core.designsystem.RecordStep
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RecordRegisterUiState(
    val currentStep: RecordStep = RecordStep.QUOTE,
    val recordPageState: TextFieldState = TextFieldState(),
    val recordSentenceState: TextFieldState = TextFieldState(),
    val selectedEmotion: String = "",
    val impressionState: TextFieldState = TextFieldState(),
    val impressionGuideList: ImmutableList<String> = persistentListOf(),
    val selectedImpressionGuide: String = "",
    val isImpressionGuideBottomSheetVisible: Boolean = false,
    val isExitDialogVisible: Boolean = false,
    val eventSink: (RecordRegisterUiEvent) -> Unit,
) : CircuitUiState

sealed interface RecordRegisterUiEvent : CircuitUiEvent {
    data object OnBackButtonClick : RecordRegisterUiEvent
    data object OnClearClick : RecordRegisterUiEvent
    data object OnNextButtonClick : RecordRegisterUiEvent
    data object OnSentenceScanButtonClick : RecordRegisterUiEvent
    data object OnSelectEmotion : RecordRegisterUiEvent
    data object OnImpressionGuideButtonClick : RecordRegisterUiEvent
    data object OnImpressionGuideBottomSheetDismiss : RecordRegisterUiEvent
    data class OnSelectImpressionGuide(val index: Int) : RecordRegisterUiEvent
    data object OnSelectionConfirmed : RecordRegisterUiEvent
    data object OnExitDialogConfirm : RecordRegisterUiEvent
    data object OnExitDialogDismiss : RecordRegisterUiEvent
}
