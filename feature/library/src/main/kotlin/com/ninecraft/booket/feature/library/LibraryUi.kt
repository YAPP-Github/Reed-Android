package com.ninecraft.booket.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.feature.library.component.FilterChipGroup
import com.ninecraft.booket.feature.library.component.LibraryBookItem
import com.ninecraft.booket.feature.library.component.LibraryHeader
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf

@CircuitInject(LibraryScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun LibraryUi(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    HandleLibrarySideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LibraryContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Composable
internal fun LibraryContent(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LibraryHeader(
            onSearchClick = {
                // TODO: 내서재 검색 화면으로 이동
            },
            onSettingClick = {
                state.eventSink(LibraryUiEvent.OnSettingsClick)
            },
        )
        FilterChipGroup(
            filterList = state.filterChips,
            selectedChipOption = state.currentFilter,
            onChipClick = { status ->
                state.eventSink(LibraryUiEvent.OnFilterClick(status))
            },
        )

        when (state.uiState) {
            is UiState.Idle -> {
                EmptyResult()
            }

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = ReedTheme.colors.contentBrand)
                }
            }

            is UiState.Success -> {
                if (state.books.isEmpty()) {
                    EmptyResult()
                } else {
                    InfinityLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        loadMore = {
                            state.eventSink(LibraryUiEvent.OnLoadMore)
                        },
                    ) {
                        items(state.books) {
                            LibraryBookItem(
                                book = it,
                                onBookClick = {
                                    state.eventSink(LibraryUiEvent.OnBookClick(it.userBookId))
                                },
                            )
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ReedTheme.colors.borderPrimary),
                            )
                        }
                        item {
                            LoadStateFooter(
                                footerState = state.footerState,
                                onRetryClick = { state.eventSink(LibraryUiEvent.OnLoadMore) },
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                ErrorResult(state = state, errorMessage = state.uiState.message)
            }
        }
    }
}

@Composable
private fun EmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.library_empty_book_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.headline1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Text(
                text = stringResource(R.string.library_empty_book_description),
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.body1Medium,
            )
        }
    }
}

@Composable
private fun ErrorResult(state: LibraryUiState, errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.library_error_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.headline1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Text(
                text = errorMessage,
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            ReedButton(
                onClick = {
                    state.eventSink(LibraryUiEvent.OnRetryClick)
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                text = stringResource(R.string.library_retry),
            )
        }
    }
}

@DevicePreview
@Composable
private fun LibraryPreview() {
    ReedTheme {
        LibraryUi(
            state = LibraryUiState(
                uiState = UiState.Success,
                books = persistentListOf(
                    LibraryBookSummaryModel(
                        bookTitle = "코틀린을 활용한 안드로이드 프로그래밍",
                        bookAuthor = "우재남, 유혜림",
                        coverImageUrl = "https://image.aladin.co.kr/product/24342/42/coversum/k542630705_1.jpg",
                        publisher = "한빛아카데미",
                    ),
                ),
                eventSink = {},
            ),
        )
    }
}
