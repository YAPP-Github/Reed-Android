package com.ninecraft.booket.feature.search.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.component.textfield.ReedTextField
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.feature.screens.LibrarySearchScreen
import com.ninecraft.booket.feature.search.R
import com.ninecraft.booket.feature.search.common.component.RecentSearchTitle
import com.ninecraft.booket.feature.search.common.component.SearchItem
import com.ninecraft.booket.feature.search.library.component.LibraryBookItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(LibrarySearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun LibrarySearchUi(
    state: LibrarySearchUiState,
    modifier: Modifier = Modifier,
) {
    HandlingLibrarySearchSideEffect(state = state)

    ReedScaffold(
        modifier = modifier,
        containerColor = White,
    ) { innerPadding ->
        LibrarySearchContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@Composable
internal fun LibrarySearchContent(
    state: LibrarySearchUiState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.library_search_title),
            onBackClick = {
                state.eventSink(LibrarySearchUiEvent.OnBackClick)
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        ReedTextField(
            queryState = state.queryState,
            queryHintRes = R.string.library_search_hint,
            onSearch = { text ->
                state.eventSink(LibrarySearchUiEvent.OnSearchClick(text))
            },
            onClear = {
                state.eventSink(LibrarySearchUiEvent.OnClearClick)
            },
            modifier = modifier.padding(horizontal = ReedTheme.spacing.spacing5),
            backgroundColor = ReedTheme.colors.baseSecondary,
            borderStroke = null,
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = state.uiState.message,
                        style = ReedTheme.typography.body1Regular,
                    )
                    Button(
                        onClick = { state.eventSink(LibrarySearchUiEvent.OnRetryClick) },
                        modifier = Modifier.padding(top = ReedTheme.spacing.spacing3),
                    ) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
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
                                        state.eventSink(LibrarySearchUiEvent.OnRecentSearchClick(keyword))
                                    },
                                    onRemoveIconClick = { keyword ->
                                        state.eventSink(LibrarySearchUiEvent.OnRecentSearchRemoveClick(keyword))
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
                if (state.books.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.library_empty_result),
                            color = ReedTheme.colors.contentSecondary,
                            style = ReedTheme.typography.body1Medium,
                        )
                    }
                } else {
                    InfinityLazyColumn(
                        loadMore = {
                            state.eventSink(LibrarySearchUiEvent.OnLoadMore)
                        },
                    ) {
                        items(
                            count = state.books.size,
                            key = { index -> state.books[index].bookIsbn },
                        ) { index ->
                            Column {
                                LibraryBookItem(
                                    book = state.books[index],
                                    onBookClick = { book ->
                                        state.eventSink(
                                            LibrarySearchUiEvent.OnBookClick(
                                                userBookId = book.userBookId,
                                                isbn13 = book.bookIsbn,
                                            ),
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

                        item {
                            LoadStateFooter(
                                footerState = state.footerState,
                                onRetryClick = { state.eventSink(LibrarySearchUiEvent.OnLoadMore) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun LibrarySearchPreview() {
    ReedTheme {
        LibrarySearchUi(
            state = LibrarySearchUiState(
                eventSink = {},
            ),
        )
    }
}
