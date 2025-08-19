package com.ninecraft.booket.feature.search.book

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@Immutable
sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data object Success : UiState
    data class Error(val exception: Throwable) : UiState
}

data class BookSearchUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val queryState: TextFieldState = TextFieldState(),
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val searchResult: BookSearchModel = BookSearchModel(),
    val books: ImmutableList<BookSummaryModel> = persistentListOf(),
    val selectedBookIsbn: String = "",
    val isBookRegisterBottomSheetVisible: Boolean = false,
    val selectedBookStatus: BookStatus? = null,
    val isBookRegisterSuccessBottomSheetVisible: Boolean = false,
    val sideEffect: BookSearchSideEffect? = null,
    val eventSink: (BookSearchUiEvent) -> Unit,
) : CircuitUiState {
    val isEmptySearchResult: Boolean get() = uiState is UiState.Success && searchResult.totalResults == 0
}

@Immutable
sealed interface BookSearchSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : BookSearchSideEffect
}

sealed interface BookSearchUiEvent : CircuitUiEvent {
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
}

enum class BookRegisteredState(val value: String) {
    BEFORE_REGISTRATION("BEFORE_REGISTRATION"),
    ;

    companion object {
        fun from(value: String?): BookRegisteredState? {
            return entries.find { it.value == value }
        }
    }
}
