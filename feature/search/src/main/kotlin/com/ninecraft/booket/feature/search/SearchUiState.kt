package com.ninecraft.booket.feature.search

import androidx.compose.foundation.text.input.TextFieldState
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
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

data class SearchUiState(
    val uiState: UiState = UiState.Idle,
    val footerState: FooterState = FooterState.Idle,
    val queryState: TextFieldState = TextFieldState(""),
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val searchResult: BookSearchModel = BookSearchModel(),
    val books: ImmutableList<BookSummaryModel> = persistentListOf(),
    val startIndex: Int = 0,
    val isLastPage: Boolean = false,
    val selectedBookIsbn: String = "",
    val isBookRegisterBottomSheetVisible: Boolean = false,
    val selectedBookStatus: BookStatus? = null,
    val isBookRegisterSuccessBottomSheetVisible: Boolean = false,
    val sideEffect: SearchSideEffect? = null,
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState {
    val isEmptySearchResult: Boolean get() = uiState is UiState.Success && searchResult.totalResults == 0
}

sealed interface SearchSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : SearchSideEffect
}

sealed interface SearchUiEvent : CircuitUiEvent {
    data object OnBackClick : SearchUiEvent
    data class OnRecentSearchClick(val query: String) : SearchUiEvent
    data class OnRecentSearchRemoveClick(val query: String) : SearchUiEvent
    data class OnSearchClick(val text: String) : SearchUiEvent
    data object OnClearClick : SearchUiEvent
    data class OnBookClick(val bookIsbn: String) : SearchUiEvent
    data object OnLoadMore : SearchUiEvent
    data object OnRetryClick : SearchUiEvent
    data object OnBookRegisterBottomSheetDismiss : SearchUiEvent
    data class OnBookStatusSelect(val bookStatus: BookStatus) : SearchUiEvent
    data object OnBookRegisterSuccessBottomSheetDismiss : SearchUiEvent
    data object OnBookRegisterButtonClick : SearchUiEvent
    data object OnBookRegisterSuccessOkButtonClick : SearchUiEvent
    data object OnBookRegisterSuccessCancelButtonClick : SearchUiEvent
}

enum class BookStatus(val value: String) {
    BEFORE_READING("BEFORE_READING"),
    READING("READING"),
    COMPLETED("COMPLETED"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            BEFORE_READING -> R.string.book_status_before
            READING -> R.string.book_status_reading
            COMPLETED -> R.string.book_status_completed
        }
    }

    companion object Companion {
        fun fromValue(value: String): BookStatus? {
            return entries.find { it.value == value }
        }
    }
}
