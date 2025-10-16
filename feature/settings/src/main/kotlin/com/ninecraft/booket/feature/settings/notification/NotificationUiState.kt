package com.ninecraft.booket.feature.settings.notification

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class NotificationUiState(
    val isNotificationEnabled: Boolean = false,
    val eventSink: (NotificationUiEvent) -> Unit,
) : CircuitUiState

sealed interface NotificationUiEvent : CircuitUiEvent {
    data object OnBackClick : NotificationUiEvent
    data class OnNotificationToggle(val enabled: Boolean) : NotificationUiEvent
}
