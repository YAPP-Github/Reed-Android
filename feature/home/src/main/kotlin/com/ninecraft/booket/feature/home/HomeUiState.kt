package com.ninecraft.booket.feature.home

import com.ninecraft.booket.core.model.RecentBookModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val recentBooks: ImmutableList<RecentBookModel> = persistentListOf(),
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

sealed interface HomeUiEvent : CircuitUiEvent {
    data object OnSettingsClick : HomeUiEvent
    data object OnBookRegisterClick : HomeUiEvent
    data class OnRecordButtonClick(val userBookId: String) : HomeUiEvent
    data object OnBookDetailClick : HomeUiEvent
}
