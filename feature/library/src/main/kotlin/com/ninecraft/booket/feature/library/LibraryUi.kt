package com.ninecraft.booket.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.toErrorType
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.mediumButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.core.ui.component.ReedErrorUi
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.feature.library.component.FilterChipGroup
import com.ninecraft.booket.feature.library.component.LibraryBookItem
import com.ninecraft.booket.feature.library.component.LibraryHeader
import com.ninecraft.booket.feature.screens.LibraryScreen
import com.ninecraft.booket.feature.screens.component.MainBottomBar
import com.ninecraft.booket.feature.screens.component.MainTab
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@TraceRecomposition
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

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                tabs = MainTab.entries.toImmutableList(),
                currentTab = MainTab.LIBRARY,
                onTabSelected = { tab ->
                    state.eventSink(LibraryUiEvent.OnTabSelected(tab))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LibraryHeader(
                onSearchClick = {
                    state.eventSink(LibraryUiEvent.OnLibrarySearchClick)
                },
                onSettingClick = {
                    state.eventSink(LibraryUiEvent.OnSettingsClick)
                },
            )

            LibraryContent(
                state = state,
                modifier = Modifier,
            )
        }
    }
}

@TraceRecomposition
@Composable
internal fun LibraryContent(
    state: LibraryUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilterChipGroup(
            filterList = state.filterChips,
            selectedChipOption = state.currentFilter,
            onChipClick = { status ->
                state.eventSink(LibraryUiEvent.OnFilterClick(status))
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))

        if (state.isGuestMode) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.library_login_required_title),
                        color = ReedTheme.colors.contentPrimary,
                        textAlign = TextAlign.Center,
                        style = ReedTheme.typography.headline1SemiBold,
                    )
                    Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
                    Text(
                        text = stringResource(R.string.library_login_required_description),
                        color = ReedTheme.colors.contentSecondary,
                        textAlign = TextAlign.Center,
                        style = ReedTheme.typography.body1Medium,
                    )
                    Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
                    ReedButton(
                        onClick = {
                            state.eventSink(LibraryUiEvent.OnLoginClick)
                        },
                        text = stringResource(R.string.login),
                        colorStyle = ReedButtonColorStyle.SECONDARY,
                        sizeStyle = mediumButtonStyle,
                    )
                }
            }
        } else {
            when (state.uiState) {
                is UiState.Idle -> {
                    EmptyResult()
                }

                is UiState.Loading -> {
                    ReedLoadingIndicator()
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
                                        state.eventSink(LibraryUiEvent.OnBookClick(it.userBookId, it.isbn13))
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
                    ReedErrorUi(
                        errorType = state.uiState.exception.toErrorType(),
                        onRetryClick = { state.eventSink(LibraryUiEvent.OnRetryClick) },
                    )
                }
            }
        }
    }
}

@TraceRecomposition
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
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.headline1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Text(
                text = stringResource(R.string.library_empty_book_description),
                color = ReedTheme.colors.contentSecondary,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.body1Medium,
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
