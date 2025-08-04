package com.ninecraft.booket.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.component.FooterState
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class LibraryPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
) : Presenter<LibraryUiState> {
    companion object {
        private const val PAGE_SIZE = 10
        private const val START_INDEX = 0
    }

    @Composable
    override fun present(): LibraryUiState {
        val scope = rememberCoroutineScope()

        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        var filterChips by rememberRetained {
            mutableStateOf(LibraryFilterOption.entries.map { LibraryFilterChip(option = it, count = 0) }.toPersistentList())
        }
        var currentFilter by rememberRetained { mutableStateOf(LibraryFilterOption.TOTAL) }
        var books by rememberRetained { mutableStateOf(persistentListOf<LibraryBookSummaryModel>()) }
        var sideEffect by rememberRetained { mutableStateOf<LibrarySideEffect?>(null) }

        var currentPage by rememberRetained { mutableIntStateOf(START_INDEX) }
        var isLastPage by rememberRetained { mutableStateOf(false) }

        fun getLibraryBooks(status: String?, page: Int, size: Int) {
            scope.launch {
                if (page == START_INDEX) {
                    uiState = UiState.Loading
                } else {
                    footerState = FooterState.Loading
                }

                repository.getLibrary(status = status, page = page, size = size)
                    .onSuccess { result ->
                        filterChips = filterChips.map { chip ->
                            when (chip.option) {
                                LibraryFilterOption.TOTAL -> chip.copy(count = result.totalCount)
                                LibraryFilterOption.BEFORE_READING -> chip.copy(count = result.beforeReadingCount)
                                LibraryFilterOption.READING -> chip.copy(count = result.readingCount)
                                LibraryFilterOption.COMPLETED -> chip.copy(count = result.completedCount)
                            }
                        }.toPersistentList()

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

        fun handleEvent(event: LibraryUiEvent) {
            when (event) {
                is LibraryUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is LibraryUiEvent.OnSettingsClick -> {
                    navigator.goTo(SettingsScreen)
                }

                is LibraryUiEvent.OnFilterClick -> {
                    currentFilter = event.filterOption
                    getLibraryBooks(status = currentFilter.getApiValue(), page = START_INDEX, size = PAGE_SIZE)
                }

                is LibraryUiEvent.OnBookClick -> {
                    navigator.goTo(BookDetailScreen(bookId = event.bookId))
                }

                is LibraryUiEvent.OnLoadMore -> {
                    if (footerState !is FooterState.Loading && !isLastPage) {
                        getLibraryBooks(status = currentFilter.getApiValue(), page = currentPage + 1, size = PAGE_SIZE)
                    }
                }

                is LibraryUiEvent.OnRetryClick -> {
                    getLibraryBooks(status = currentFilter.getApiValue(), page = currentPage, size = PAGE_SIZE)
                }
            }
        }

        LaunchedEffect(Unit) {
            if (uiState == UiState.Idle || uiState is UiState.Error) {
                getLibraryBooks(
                    status = currentFilter.getApiValue(),
                    page = START_INDEX,
                    size = PAGE_SIZE,
                )
            }
        }

        return LibraryUiState(
            uiState = uiState,
            footerState = footerState,
            filterChips = filterChips,
            currentFilter = currentFilter,
            books = books,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LibraryPresenter
    }
}
