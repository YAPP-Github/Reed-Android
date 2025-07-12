package com.ninecraft.booket.feature.search

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.screens.SearchScreen
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

@Suppress("unused")
class SearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val bookRepository: BookRepository,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        val scope = rememberCoroutineScope()
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var footerState by rememberRetained { mutableStateOf<FooterState>(FooterState.Idle) }
        val searchText = rememberTextFieldState()
        var searchResult by rememberRetained { mutableStateOf(BookSearchModel()) }
        var books by rememberRetained { mutableStateOf(persistentListOf<BookSummaryModel>()) }
        var currentPage by rememberRetained { mutableStateOf(0) }
        var isLastPage by rememberRetained { mutableStateOf(false) }

        fun searchBooks(query: String, currentOffset: Int = 0) {
            scope.launch {
                try {
                    if (currentOffset == 0) {
                        uiState = UiState.Loading
                    } else {
                        footerState = FooterState.Loading
                    }

                    bookRepository.searchBook(query = query, start = currentOffset)
                        .onSuccess { result ->
                            searchResult = result
                            books = if (currentOffset == 0) {
                                result.books.toPersistentList()
                            } else {
                                (books + result.books).toPersistentList()
                            }

                            currentPage = currentOffset
                            isLastPage = result.books.size < 20
                            uiState = UiState.Idle
                            footerState = if (isLastPage) FooterState.End else FooterState.Idle
                        }
                        .onFailure { exception ->
                            Logger.d(exception)
                            if (currentOffset == 0) {
                                uiState = UiState.Error(exception.message ?: "알 수 없는 오류가 발생했습니다.")
                            } else {
                                footerState = FooterState.Error(exception.message ?: "알 수 없는 오류가 발생했습니다.")
                            }
                        }
                } catch (e: Exception) {
                    Logger.d(e)
                    if (currentOffset == 0) {
                        uiState = UiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                    } else {
                        footerState = FooterState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                    }
                }
            }
        }

        fun handleEvent(event: SearchUiEvent) {
            when (event) {
                is SearchUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is SearchUiEvent.OnSearch -> {
                    searchBooks(query = event.text, currentOffset = 0)
                }

                is SearchUiEvent.OnLoadMore -> {
                    if (footerState !is FooterState.Loading && !isLastPage && searchText.text.toString().isNotEmpty()) {
                        searchBooks(query = searchText.text.toString(), currentOffset = currentPage + 1)
                    }
                }

                is SearchUiEvent.OnRetryClick -> {
                    if (searchText.text.toString().isNotEmpty()) {
                        searchBooks(query = searchText.text.toString(), currentOffset = 0)
                    }
                }

                is SearchUiEvent.OnBookClick -> {}
            }
        }

        return SearchUiState(
            uiState = uiState,
            footerState = footerState,
            searchText = searchText,
            searchResult = searchResult,
            books = books,
            offset = currentPage,
            isLastPage = isLastPage,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SearchPresenter
    }
}
