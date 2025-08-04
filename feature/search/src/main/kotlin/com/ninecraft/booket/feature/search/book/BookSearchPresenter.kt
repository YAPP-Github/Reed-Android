package com.ninecraft.booket.feature.search.book

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SearchScreen
import com.ninecraft.booket.feature.screens.delayedGoTo
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class BookSearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
) : Presenter<BookSearchUiState> {
    companion object {
        private const val PAGE_SIZE = 20
        private const val START_INDEX = 1
    }

    @Composable
    override fun present(): BookSearchUiState {
        val scope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        val queryState = rememberTextFieldState()
        val recentSearches by repository.bookRecentSearches.collectAsRetainedState(initial = emptyList())
        var searchResult by rememberRetained { mutableStateOf(BookSearchModel()) }
        var books by rememberRetained { mutableStateOf(persistentListOf<BookSummaryModel>()) }
        var currentStartIndex by rememberRetained { mutableIntStateOf(START_INDEX) }
        var isLastPage by rememberRetained { mutableStateOf(false) }
        var selectedBookIsbn by rememberRetained { mutableStateOf("") }
        var registeredUserBookId by rememberRetained { mutableStateOf("") }
        var isBookRegisterBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var selectedBookStatus by rememberRetained { mutableStateOf<BookStatus?>(null) }
        var isBookRegisterSuccessBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<BookSearchSideEffect?>(null) }

        fun searchBooks(query: String, startIndex: Int = START_INDEX) {
            scope.launch {
                if (startIndex == START_INDEX) {
                    uiState = UiState.Loading
                } else {
                    footerState = FooterState.Loading
                }

                repository.searchBook(query = query, start = startIndex)
                    .onSuccess { result ->
                        searchResult = result
                        books = if (startIndex == START_INDEX) {
                            result.books.toPersistentList()
                        } else {
                            (books + result.books).toPersistentList()
                        }

                        currentStartIndex = startIndex
                        isLastPage = result.books.size < PAGE_SIZE

                        if (startIndex == START_INDEX) {
                            uiState = UiState.Success
                        } else {
                            footerState = if (isLastPage) FooterState.End else FooterState.Idle
                        }
                    }
                    .onFailure { exception ->
                        Logger.d(exception)
                        val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다."
                        if (startIndex == START_INDEX) {
                            uiState = UiState.Error(errorMessage)
                        } else {
                            footerState = FooterState.Error(errorMessage)
                        }
                    }
            }
        }

        fun upsertBook(bookIsbn: String, bookStatus: String) {
            scope.launch {
                repository.upsertBook(bookIsbn, bookStatus)
                    .onSuccess {
                        registeredUserBookId = it.userBookId
                        books = books.map { book ->
                            if (book.isbn13 == selectedBookIsbn) {
                                book.copy(userBookStatus = bookStatus)
                            } else book
                        }.toPersistentList()

                        selectedBookIsbn = ""
                        selectedBookStatus = null
                        isBookRegisterBottomSheetVisible = false
                        isBookRegisterSuccessBottomSheetVisible = true
                    }
                    .onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = BookSearchSideEffect.ShowToast(message)
                        }

                        handleException(
                            exception = exception,
                            onError = handleErrorMessage,
                            onLoginRequired = {
                                navigator.resetRoot(LoginScreen)
                            },
                        )
                    }
            }
        }

        fun handleEvent(event: BookSearchUiEvent) {
            when (event) {
                is BookSearchUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is BookSearchUiEvent.OnRecentSearchClick -> {
                    queryState.edit {
                        replace(0, length, "")
                        append(event.query)
                    }
                    searchBooks(query = event.query, startIndex = START_INDEX)
                }

                is BookSearchUiEvent.OnRecentSearchRemoveClick -> {
                    scope.launch {
                        repository.removeBookRecentSearch(query = event.query)
                    }
                }

                is BookSearchUiEvent.OnSearchClick -> {
                    val query = event.text.trim()
                    if (query.isNotEmpty()) {
                        searchBooks(query = event.text, startIndex = START_INDEX)
                    }
                }

                is BookSearchUiEvent.OnClearClick -> {
                    queryState.clearText()
                }

                is BookSearchUiEvent.OnLoadMore -> {
                    val query = queryState.text.trim().toString()
                    if (footerState !is FooterState.Loading && !isLastPage && query.isNotEmpty()) {
                        searchBooks(query = queryState.text.toString(), startIndex = currentStartIndex + 1)
                    }
                }

                is BookSearchUiEvent.OnRetryClick -> {
                    val query = queryState.text.trim().toString()
                    if (query.isNotEmpty()) {
                        searchBooks(query = queryState.text.toString(), startIndex = START_INDEX)
                    }
                }

                is BookSearchUiEvent.OnBookClick -> {
                    selectedBookIsbn = event.bookIsbn
                    isBookRegisterBottomSheetVisible = true
                }

                is BookSearchUiEvent.OnBookRegisterBottomSheetDismiss -> {
                    isBookRegisterBottomSheetVisible = false
                    selectedBookIsbn = ""
                    selectedBookStatus = null
                }

                is BookSearchUiEvent.OnBookStatusSelect -> {
                    selectedBookStatus = event.bookStatus
                }

                is BookSearchUiEvent.OnBookRegisterButtonClick -> {
                    selectedBookStatus?.let { bookStatus -> upsertBook(selectedBookIsbn, bookStatus.value) }
                }

                is BookSearchUiEvent.OnBookRegisterSuccessBottomSheetDismiss -> {
                    isBookRegisterSuccessBottomSheetVisible = false
                }

                is BookSearchUiEvent.OnBookRegisterSuccessOkButtonClick -> {
                    isBookRegisterSuccessBottomSheetVisible = false
                    scope.launch {
                        navigator.delayedGoTo(RecordScreen(registeredUserBookId))
                    }
                }

                is BookSearchUiEvent.OnBookRegisterSuccessCancelButtonClick -> {
                    isBookRegisterSuccessBottomSheetVisible = false
                }
            }
        }

        return BookSearchUiState(
            uiState = uiState,
            footerState = footerState,
            queryState = queryState,
            recentSearches = recentSearches.toImmutableList(),
            searchResult = searchResult,
            books = books,
            startIndex = currentStartIndex,
            isLastPage = isLastPage,
            selectedBookIsbn = selectedBookIsbn,
            isBookRegisterBottomSheetVisible = isBookRegisterBottomSheetVisible,
            selectedBookStatus = selectedBookStatus,
            isBookRegisterSuccessBottomSheetVisible = isBookRegisterSuccessBottomSheetVisible,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): BookSearchPresenter
    }
}
