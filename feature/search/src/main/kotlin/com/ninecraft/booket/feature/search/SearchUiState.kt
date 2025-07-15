package com.ninecraft.booket.feature.search

import androidx.compose.foundation.text.input.TextFieldState
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}

sealed interface FooterState {
    data object Idle : FooterState
    data object Loading : FooterState
    data object End : FooterState
    data class Error(val message: String) : FooterState
}

data class SearchUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val queryState: TextFieldState = TextFieldState(""),
    val searchResult: BookSearchModel = BookSearchModel(),
    val books: ImmutableList<BookSummaryModel> = persistentListOf(),
    val startIndex: Int = 0,
    val isLastPage: Boolean = false,
    val sideEffect: SearchSideEffect? = null,
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState {
    val isEmptyResult: Boolean get() = uiState is UiState.Success && searchResult.totalResults == 0
}

sealed interface SearchSideEffect {
    data class ShowToast(val message: String) : SearchSideEffect
}

sealed interface SearchUiEvent : CircuitUiEvent {
    data object OnBackClick : SearchUiEvent
    data class OnSearch(val text: String) : SearchUiEvent
    data class OnBookClick(val bookIsbn: String) : SearchUiEvent
    data object OnLoadMore : SearchUiEvent
    data object OnRetryClick : SearchUiEvent
}
