package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionGroupModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface EmotionUiState {
    data object idle : EmotionUiState
    data object Loading : EmotionUiState
    data object Success : EmotionUiState
    data class Error(val exception: Throwable) : EmotionUiState
}

data class EmotionEditUiState(
    val emotionUiState: EmotionUiState = EmotionUiState.idle,
    val isEditButtonEnabled: Boolean = false,
    val emotionGroups: ImmutableList<EmotionGroupModel> = persistentListOf(),
    val selectedEmotionCode: EmotionCode? = null,
    val selectedEmotionMap: Map<EmotionCode, ImmutableList<String>> = emptyMap(),
    val committedEmotion: EmotionCode? = null,
    val committedEmotionMap: Map<EmotionCode, ImmutableList<String>> = emptyMap(),
    val isEmotionDetailBottomSheetVisible: Boolean = false,
    val eventSink: (EmotionEditUiEvent) -> Unit,
) : CircuitUiState

sealed interface EmotionEditUiEvent : CircuitUiEvent {
    data object OnBackClick : EmotionEditUiEvent
    data class OnSelectEmotionCode(val emotionCode: EmotionCode) : EmotionEditUiEvent
    data class OnEmotionDetailToggled(val detailId: String) : EmotionEditUiEvent
    data class OnEmotionDetailRemoved(val detailId: String) : EmotionEditUiEvent
    data object OnEmotionDetailCommitted : EmotionEditUiEvent
    data object OnEmotionDetailSkipped : EmotionEditUiEvent
    data object OnEmotionDetailBottomSheetDismiss : EmotionEditUiEvent
    data object OnEditButtonClick : EmotionEditUiEvent
    data object OnRetryGetEmotions : EmotionEditUiEvent
}
