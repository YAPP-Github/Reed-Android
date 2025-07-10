package com.ninecraft.booket.feature.settings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SettingsUiState(
    val isLogoutBottomSheetVisible: Boolean,
    val isWithdrawBottomSheetVisible: Boolean,
    val isWithdrawConfirmed: Boolean,
    val eventSink: (SettingsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SettingsUiEvent : CircuitUiEvent {
    data object OnBackClick : SettingsUiEvent
    data class OnTermDetailClick(val title: String) : SettingsUiEvent
    data object OnLogoutClick : SettingsUiEvent
    data object OnWithdrawClick : SettingsUiEvent
    data object OnBottomSheetDismissed : SettingsUiEvent
    data object OnWithdrawConfirmationToggled : SettingsUiEvent
    data object Logout : SettingsUiEvent
    data object Withdraw : SettingsUiEvent
}
