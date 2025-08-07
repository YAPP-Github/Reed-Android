package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.feature.screens.component.MainTab
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class HomeUiState(
    val recentBooks: ImmutableList<RecentBookModel> = persistentListOf(),
    val sideEffect: HomeSideEffect? = null,
    val eventSink: (HomeUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface HomeSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : HomeSideEffect
}

sealed interface HomeUiEvent : CircuitUiEvent {
    data object OnSettingsClick : HomeUiEvent
    data object OnBookRegisterClick : HomeUiEvent
    data class OnRecordButtonClick(val userBookId: String) : HomeUiEvent
    data class OnBookDetailClick(
        val userBookId: String,
        val isbn: String,
    ) : HomeUiEvent
    data class OnTabSelected(val tab: MainTab) : HomeUiEvent
}
