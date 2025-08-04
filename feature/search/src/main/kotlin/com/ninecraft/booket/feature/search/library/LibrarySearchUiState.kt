package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.text.input.TextFieldState
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

data class LibrarySearchUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val queryState: TextFieldState = TextFieldState(),
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val books: ImmutableList<LibraryBookSummaryModel> = persistentListOf(),
    val sideEffect: LibrarySearchSideEffect? = null,
    val eventSink: (LibrarySearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface LibrarySearchSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : LibrarySearchSideEffect
}

sealed interface LibrarySearchUiEvent : CircuitUiEvent {
    data object OnBackClick : LibrarySearchUiEvent
    data class OnRecentSearchClick(val query: String) : LibrarySearchUiEvent
    data class OnRecentSearchRemoveClick(val query: String) : LibrarySearchUiEvent
    data class OnSearchClick(val query: String) : LibrarySearchUiEvent
    data object OnClearClick : LibrarySearchUiEvent
    data object OnLoadMore : LibrarySearchUiEvent
    data object OnRetryClick : LibrarySearchUiEvent
    data class OnBookClick(val userBookId: String, val isbn: String) : LibrarySearchUiEvent
}
