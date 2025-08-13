package com.ninecraft.booket.feature.settings.osslicenses

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class OssLicensesUiState(
    val eventSink: (OssLicensesUiEvent) -> Unit,
) : CircuitUiState

sealed interface OssLicensesUiEvent : CircuitUiEvent {
    data object OnBackClicked : OssLicensesUiEvent
}
