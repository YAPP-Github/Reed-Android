package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.LibrarySearchScreen
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

class LibrarySearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
) : Presenter<LibrarySearchUiState> {
    companion object {
        private const val PAGE_SIZE = 20
        private const val START_INDEX = 0
    }

    @Composable
    override fun present(): LibrarySearchUiState {
        val scope = rememberCoroutineScope()

        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        val queryState = rememberTextFieldState()
        val recentSearches by repository.libraryRecentSearches.collectAsRetainedState(initial = emptyList())
        var books by rememberRetained { mutableStateOf(persistentListOf<LibraryBookSummaryModel>()) }

        var currentPage by rememberRetained { mutableIntStateOf(START_INDEX) }
        var isLastPage by rememberRetained { mutableStateOf(false) }

        fun searchLibraryBooks(query: String, page: Int, size: Int) {
            scope.launch {
                if (page == START_INDEX) {
                    uiState = UiState.Loading
                } else {
                    footerState = FooterState.Loading
                }

                repository.searchLibrary(title = query, page = page, size = size)
                    .onSuccess { result ->
                        books = if (result.books.page.number == START_INDEX) {
                            result.books.content.toPersistentList()
                        } else {
                            (books + result.books.content).toPersistentList()
                        }

                        currentPage = page
                        isLastPage = result.books.page.number == result.books.page.totalPages - 1

                        if (page == START_INDEX) {
                            uiState = UiState.Success
                            footerState = FooterState.Idle
                        } else {
                            footerState = if (isLastPage) FooterState.End else FooterState.Idle
                        }
                    }
                    .onFailure { exception ->
                        Logger.d(exception)
                        val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다."
                        if (page == START_INDEX) {
                            uiState = UiState.Error(errorMessage)
                        } else {
                            footerState = FooterState.Error(errorMessage)
                        }
                    }
            }
        }

        fun handleEvent(event: LibrarySearchUiEvent) {
            when (event) {
                is LibrarySearchUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is LibrarySearchUiEvent.OnRecentSearchClick -> {
                    searchLibraryBooks(query = event.query, page = START_INDEX, size = PAGE_SIZE)
                }

                is LibrarySearchUiEvent.OnRecentSearchRemoveClick -> {
                    scope.launch {
                        repository.removeLibraryRecentSearch(event.query)
                    }
                }

                is LibrarySearchUiEvent.OnSearchClick -> {
                    val query = event.query.trim()
                    if (query.isNotEmpty()) {
                        searchLibraryBooks(query = query, page = START_INDEX, size = PAGE_SIZE)
                    }
                }

                is LibrarySearchUiEvent.OnClearClick -> {
                    queryState.clearText()
                }

                is LibrarySearchUiEvent.OnLoadMore -> {
                    val query = queryState.text.trim().toString()
                    if (footerState !is FooterState.Loading && !isLastPage && query.isNotEmpty()) {
                        searchLibraryBooks(query = query, page = currentPage + 1, size = PAGE_SIZE)
                    }
                }

                is LibrarySearchUiEvent.OnRetryClick -> {
                    val query = queryState.text.trim().toString()
                    if (query.isNotEmpty()) {
                        searchLibraryBooks(query = queryState.text.toString(), page = START_INDEX, size = PAGE_SIZE)
                    }
                }

                is LibrarySearchUiEvent.OnBookClick -> {
                    val userBookId = event.userBookId
                    val isbn = event.isbn
                    // TODO: 도서 상세 화면에 bookIsbn, userBookId 넘겨야 함
                    navigator.goTo(BookDetailScreen(isbn = isbn))
                }
            }
        }

        return LibrarySearchUiState(
            uiState = uiState,
            footerState = footerState,
            queryState = queryState,
            recentSearches = recentSearches.toImmutableList(),
            books = books,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LibrarySearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LibrarySearchPresenter
    }
}
