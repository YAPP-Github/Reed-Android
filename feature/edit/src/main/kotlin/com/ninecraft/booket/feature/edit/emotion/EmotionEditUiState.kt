package com.ninecraft.booket.feature.edit.emotion

import com.ninecraft.booket.core.designsystem.EmotionTag
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EmotionEditUiState(
    val selectedEmotion: String = "",
    val isEditButtonEnabled: Boolean = false,
    val emotionTags: ImmutableList<EmotionTag> = persistentListOf(),
    val eventSink: (EmotionEditUiEvent) -> Unit,
) : CircuitUiState

sealed interface EmotionEditUiEvent : CircuitUiEvent {
    data object OnBackClick : EmotionEditUiEvent
    data class OnSelectEmotion(val emotion: String) : EmotionEditUiEvent
    data object OnEditButtonClick : EmotionEditUiEvent
}
