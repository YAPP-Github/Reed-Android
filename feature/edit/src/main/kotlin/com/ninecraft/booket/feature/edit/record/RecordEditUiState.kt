package com.ninecraft.booket.feature.edit.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.feature.screens.arguments.RecordEditArgs
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class RecordEditUiState(
    val recordInfo: RecordEditArgs,
    val recordPageState: TextFieldState = TextFieldState(),
    val recordQuoteState: TextFieldState = TextFieldState(),
    val recordImpressionState: TextFieldState = TextFieldState(),
    val isPageError: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
    val sideEffect: RecordEditSideEffect? = null,
    val eventSink: (RecordEditUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface RecordEditSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : RecordEditSideEffect
}

sealed interface RecordEditUiEvent : CircuitUiEvent {
    data object OnCloseClick : RecordEditUiEvent
    data object OnClearClick : RecordEditUiEvent
    data object OnEmotionEditClick : RecordEditUiEvent
    data object OnSaveButtonClick : RecordEditUiEvent
}
