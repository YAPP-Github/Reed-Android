package com.ninecraft.booket.feature.search.book

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.common.utils.UiText
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Immutable
sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Success : SearchUiState
    data class Error(val exception: Throwable) : SearchUiState
}

data class BookSearchUiState(
    val searchUiState: SearchUiState = SearchUiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val queryState: TextFieldState = TextFieldState(),
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val searchResult: BookSearchModel = BookSearchModel(),
    val books: ImmutableList<BookSummaryModel> = persistentListOf(),
    val selectedBookIsbn: String = "",
    val isBookRegisterBottomSheetVisible: Boolean = false,
    val selectedBookStatus: BookStatus? = null,
    val upsertedBookStatus: BookStatus? = null,
    val isBookRegisterSuccessBottomSheetVisible: Boolean = false,
    val isGuestMode: Boolean = false,
    val sideEffect: BookSearchSideEffect? = null,
    val eventSink: (BookSearchUiEvent) -> Unit,
) : CircuitUiState {
    val isEmptySearchResult: Boolean get() = searchUiState is SearchUiState.Success && searchResult.totalResults == 0
}

@Immutable
sealed interface BookSearchSideEffect {
    data class ShowToast(
        val message: UiText,
        private val key: String = UUID.randomUUID().toString(),
    ) : BookSearchSideEffect

    data object NavigateToKakaoTalkChannel : BookSearchSideEffect
}

sealed interface BookSearchUiEvent : CircuitUiEvent {
    data object InitSideEffect : BookSearchUiEvent
    data object OnBackClick : BookSearchUiEvent
    data class OnRecentSearchClick(val query: String) : BookSearchUiEvent
    data class OnRecentSearchDeleteClick(val query: String) : BookSearchUiEvent
    data class OnSearchClick(val query: String) : BookSearchUiEvent
    data object OnClearClick : BookSearchUiEvent
    data class OnBookClick(val isbn13: String) : BookSearchUiEvent
    data object OnLoadMore : BookSearchUiEvent
    data object OnRetryClick : BookSearchUiEvent
    data object OnBookRegisterBottomSheetDismiss : BookSearchUiEvent
    data class OnBookStatusSelect(val bookStatus: BookStatus) : BookSearchUiEvent
    data object OnBookRegisterSuccessBottomSheetDismiss : BookSearchUiEvent
    data object OnBookRegisterButtonClick : BookSearchUiEvent
    data object OnBookRegisterSuccessOkButtonClick : BookSearchUiEvent
    data object OnBookRegisterSuccessCancelButtonClick : BookSearchUiEvent
    data object OnInquireClick : BookSearchUiEvent
}
