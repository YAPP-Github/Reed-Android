package com.ninecraft.booket.feature.settings

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import java.util.UUID

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isWithdrawBottomSheetVisible: Boolean = false,
    val isWithdrawConfirmed: Boolean = false,
    val latestVersion: String = "",
    val sideEffect: SettingsSideEffect? = null,
    val eventSink: (SettingsUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface SettingsSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : SettingsSideEffect
}

sealed interface SettingsUiEvent : CircuitUiEvent {
    data object InitSideEffect : SettingsUiEvent
    data object OnBackClick : SettingsUiEvent
    data object OnPolicyClick : SettingsUiEvent
    data object OnTermClick : SettingsUiEvent
    data object OnOssLicensesClick : SettingsUiEvent
    data object OnLogoutClick : SettingsUiEvent
    data object OnWithdrawClick : SettingsUiEvent
    data object OnBottomSheetDismissed : SettingsUiEvent
    data object OnWithdrawConfirmationToggled : SettingsUiEvent
    data object Logout : SettingsUiEvent
    data object Withdraw : SettingsUiEvent
}
