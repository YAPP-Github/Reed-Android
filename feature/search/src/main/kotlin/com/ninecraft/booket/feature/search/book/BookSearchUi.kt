package com.ninecraft.booket.feature.search.book

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.component.textfield.ReedTextField
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedErrorUi
import com.ninecraft.booket.feature.screens.SearchScreen
import com.ninecraft.booket.feature.search.R
import com.ninecraft.booket.feature.search.book.component.BookItem
import com.ninecraft.booket.feature.search.book.component.BookRegisterBottomSheet
import com.ninecraft.booket.feature.search.book.component.BookRegisterSuccessBottomSheet
import com.ninecraft.booket.feature.search.common.component.RecentSearchTitle
import com.ninecraft.booket.feature.search.common.component.SearchItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun SearchUi(
    state: BookSearchUiState,
    modifier: Modifier = Modifier,
) {
    HandleBookSearchSideEffects(state = state)

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedBackTopAppBar(
                title = stringResource(R.string.search_title),
                onBackClick = {
                    state.eventSink(BookSearchUiEvent.OnBackClick)
                },
            )
            SearchContent(
                state = state,
                modifier = Modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchContent(
    state: BookSearchUiState,
    modifier: Modifier = Modifier,
) {
    val bookRegisterBottomSheetState = rememberModalBottomSheetState()
    val bookRegisterSuccessBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        ReedTextField(
            queryState = state.queryState,
            queryHintRes = designR.string.search_book_hint,
            onSearch = { query ->
                state.eventSink(BookSearchUiEvent.OnSearchClick(query))
            },
            onClear = {
                state.eventSink(BookSearchUiEvent.OnClearClick)
            },
            modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
            borderStroke = BorderStroke(width = 1.dp, color = ReedTheme.colors.borderBrand),
            searchIconTint = ReedTheme.colors.contentBrand,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))

        ReedDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(ReedTheme.spacing.spacing2),
        )

        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

        when (state.uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = ReedTheme.colors.contentBrand)
                }
            }

            is UiState.Error -> {
                ReedErrorUi(
                    exception = state.uiState.exception,
                    onRetryClick = { state.eventSink(BookSearchUiEvent.OnRetryClick) },
                )
            }

            is UiState.Idle -> {
                if (state.recentSearches.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        RecentSearchTitle(modifier = Modifier.align(Alignment.TopCenter))
                        Text(
                            text = stringResource(R.string.empty_recent_searches),
                            modifier = Modifier.align(Alignment.Center),
                            color = ReedTheme.colors.contentSecondary,
                            style = ReedTheme.typography.body1Medium,
                        )
                    }
                } else {
                    LazyColumn {
                        item {
                            RecentSearchTitle()
                        }

                        items(
                            count = state.recentSearches.size,
                            key = { index -> state.recentSearches[index] },
                        ) { index ->
                            Column {
                                SearchItem(
                                    query = state.recentSearches[index],
                                    onQueryClick = { keyword ->
                                        state.eventSink(BookSearchUiEvent.OnRecentSearchClick(keyword))
                                    },
                                    onRemoveIconClick = { keyword ->
                                        state.eventSink(
                                            BookSearchUiEvent.OnRecentSearchRemoveClick(keyword),
                                        )
                                    },
                                )
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = ReedTheme.colors.borderPrimary,
                                )
                            }
                        }
                    }
                }
            }

            is UiState.Success -> {
                if (state.isEmptySearchResult) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.empty_results),
                            color = ReedTheme.colors.contentSecondary,
                            style = ReedTheme.typography.body1Medium,
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ReedTheme.spacing.spacing5,
                                vertical = ReedTheme.spacing.spacing2,
                            ),
                    ) {
                        Text(
                            text = stringResource(R.string.search_result_prefix),
                            color = ReedTheme.colors.contentPrimary,
                            style = ReedTheme.typography.label1Medium,
                        )
                        Text(
                            text = "${state.searchResult.totalResults}",
                            color = ReedTheme.colors.contentBrand,
                            style = ReedTheme.typography.label1Medium,
                        )
                        Text(
                            text = stringResource(R.string.search_result_suffix),
                            color = ReedTheme.colors.contentPrimary,
                            style = ReedTheme.typography.label1Medium,
                        )
                    }

                    InfinityLazyColumn(
                        loadMore = {
                            state.eventSink(BookSearchUiEvent.OnLoadMore)
                        },
                    ) {
                        items(
                            count = state.books.size,
                            key = { index -> state.books[index].key },
                        ) { index ->
                            Column {
                                BookItem(
                                    book = state.books[index],
                                    onBookClick = { book ->
                                        state.eventSink(BookSearchUiEvent.OnBookClick(book.isbn13))
                                    },
                                    enabled = BookRegisteredState.from(state.books[index].userBookStatus) == BookRegisteredState.BEFORE_REGISTRATION,
                                )
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = ReedTheme.colors.borderPrimary,
                                )
                            }
                        }

                        item {
                            LoadStateFooter(
                                footerState = state.footerState,
                                onRetryClick = { state.eventSink(BookSearchUiEvent.OnLoadMore) },
                            )
                        }
                    }
                }
            }
        }

        if (state.isBookRegisterBottomSheetVisible) {
            BookRegisterBottomSheet(
                onDismissRequest = { state.eventSink(BookSearchUiEvent.OnBookRegisterBottomSheetDismiss) },
                sheetState = bookRegisterBottomSheetState,
                onCloseButtonClick = {
                    coroutineScope.launch {
                        bookRegisterBottomSheetState.hide()
                        state.eventSink(BookSearchUiEvent.OnBookRegisterBottomSheetDismiss)
                    }
                },
                bookStatuses = BookStatus.entries.toTypedArray().toImmutableList(),
                currentBookStatus = state.selectedBookStatus,
                onItemSelected = { bookStatus ->
                    state.eventSink(
                        BookSearchUiEvent.OnBookStatusSelect(bookStatus),
                    )
                },
                onBookRegisterButtonClick = { state.eventSink(BookSearchUiEvent.OnBookRegisterButtonClick) },
            )
        }

        if (state.isBookRegisterSuccessBottomSheetVisible) {
            BookRegisterSuccessBottomSheet(
                onDismissRequest = { state.eventSink(BookSearchUiEvent.OnBookRegisterSuccessBottomSheetDismiss) },
                sheetState = bookRegisterSuccessBottomSheetState,
                onCancelButtonClick = {
                    coroutineScope.launch {
                        bookRegisterSuccessBottomSheetState.hide()
                        state.eventSink(BookSearchUiEvent.OnBookRegisterSuccessBottomSheetDismiss)
                    }
                },
                onOKButtonClick = { state.eventSink(BookSearchUiEvent.OnBookRegisterSuccessOkButtonClick) },
            )
        }
    }
}

@DevicePreview
@Composable
private fun SearchPreview() {
    ReedTheme {
        SearchUi(
            state = BookSearchUiState(
                eventSink = {},
            ),
        )
    }
}
