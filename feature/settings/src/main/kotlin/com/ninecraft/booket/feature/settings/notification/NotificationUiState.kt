package com.ninecraft.booket.feature.settings.notification

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.common.utils.UiText
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class NotificationUiState(
    val isNotificationEnabled: Boolean = false,
    val sideEffect: NotificationSideEffect? = null,
    val eventSink: (NotificationUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface NotificationSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : NotificationSideEffect
}

sealed interface NotificationUiEvent : CircuitUiEvent {
    data object InitSideEffect : NotificationUiEvent
    data object OnBackClick : NotificationUiEvent
    data class OnNotificationToggle(val enabled: Boolean) : NotificationUiEvent
}
