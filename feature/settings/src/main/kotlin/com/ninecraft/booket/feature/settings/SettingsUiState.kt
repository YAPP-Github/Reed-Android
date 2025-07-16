package com.ninecraft.booket.feature.settings

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isWithdrawBottomSheetVisible: Boolean = false,
    val isWithdrawConfirmed: Boolean = false,
    val sideEffect: SettingsSideEffect? = null,
    val eventSink: (SettingsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SettingsSideEffect {
    data class ShowToast(val message: String) : SettingsSideEffect
}

sealed interface SettingsUiEvent : CircuitUiEvent {
    data object InitSideEffect : SettingsUiEvent
    data object OnBackClick : SettingsUiEvent
    data class OnTermDetailClick(val title: String) : SettingsUiEvent
    data object OnOssLicensesClick : SettingsUiEvent
    data object OnLogoutClick : SettingsUiEvent
    data object OnWithdrawClick : SettingsUiEvent
    data object OnBottomSheetDismissed : SettingsUiEvent
    data object OnWithdrawConfirmationToggled : SettingsUiEvent
    data object Logout : SettingsUiEvent
    data object Withdraw : SettingsUiEvent
}
