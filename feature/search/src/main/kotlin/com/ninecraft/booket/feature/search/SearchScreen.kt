package com.ninecraft.booket.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.ReedTextField
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.search.component.BookItem
import com.ninecraft.booket.feature.search.component.BookRegisterBottomSheet
import com.ninecraft.booket.feature.search.component.BookRegisterSuccessBottomSheet
import com.ninecraft.booket.feature.search.component.InfinityLazyColumn
import com.ninecraft.booket.feature.search.component.LoadStateFooter
import com.ninecraft.booket.screens.SearchScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Search(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    HandleSearchSideEffects(state = state)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.search_title),
            onBackClick = {
                state.eventSink(SearchUiEvent.OnBackClick)
            },
        )
        SearchContent(
            state = state,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchContent(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val bookRegisterBottomSheetState = rememberModalBottomSheetState()
    val bookRegisterSuccessBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedTextField(
            queryState = state.queryState,
            queryHintRes = designR.string.search_book_hint,
            onSearch = { text ->
                state.eventSink(SearchUiEvent.OnSearchClick(text))
            },
            onClear = {
                state.eventSink(SearchUiEvent.OnClearClick)
            },
            modifier = modifier
                .padding(
                    vertical = ReedTheme.spacing.spacing3,
                    horizontal = ReedTheme.spacing.spacing5,
                ),
        )

        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))

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
                        onClick = { state.eventSink(SearchUiEvent.OnRetryClick) },
                        modifier = Modifier.padding(top = ReedTheme.spacing.spacing3),
                    ) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is UiState.Idle -> {
                // TODO: 최근 검색어 노출
            }

            is UiState.Success -> {
                if (state.isEmptyResult) {
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
                            state.eventSink(SearchUiEvent.OnLoadMore)
                        },
                    ) {
                        items(
                            count = state.books.size,
                            key = { index -> state.books[index].isbn },
                        ) { index ->
                            Column {
                                BookItem(
                                    book = state.books[index],
                                    onBookClick = { book ->
                                        state.eventSink(SearchUiEvent.OnBookClick(book.isbn))
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
                                onRetryClick = { state.eventSink(SearchUiEvent.OnLoadMore) },
                            )
                        }
                    }
                }
            }
        }

        if (state.isBookRegisterBottomSheetVisible) {
            BookRegisterBottomSheet(
                onDismissRequest = { state.eventSink(SearchUiEvent.OnBookRegisterBottomSheetDismiss) },
                sheetState = bookRegisterBottomSheetState,
                onCloseButtonClick = {
                    coroutineScope.launch {
                        bookRegisterBottomSheetState.hide()
                        state.eventSink(SearchUiEvent.OnBookRegisterBottomSheetDismiss)
                    }
                },
                bookStatuses = BookStatus.entries.toTypedArray().toImmutableList(),
                currentBookStatus = state.selectedBookStatus,
                onItemSelected = { bookStatus -> state.eventSink(SearchUiEvent.OnBookStatusSelect(bookStatus)) },
                onBookRegisterButtonClick = { state.eventSink(SearchUiEvent.OnBookRegisterButtonClick) },
            )
        }

        if (state.isBookRegisterSuccessBottomSheetVisible) {
            BookRegisterSuccessBottomSheet(
                onDismissRequest = { state.eventSink(SearchUiEvent.OnBookRegisterSuccessBottomSheetDismiss) },
                sheetState = bookRegisterSuccessBottomSheetState,
                onCancelButtonClick = {
                    coroutineScope.launch {
                        bookRegisterSuccessBottomSheetState.hide()
                        state.eventSink(SearchUiEvent.OnBookRegisterSuccessBottomSheetDismiss)
                    }
                },
                onOKButtonClick = { state.eventSink(SearchUiEvent.OnBookRegisterSuccessOkButtonClick) },
            )
        }
    }
}

@DevicePreview
@Composable
private fun SearchPreview() {
    ReedTheme {
        Search(
            state = SearchUiState(
                eventSink = {},
            ),
        )
    }
}
