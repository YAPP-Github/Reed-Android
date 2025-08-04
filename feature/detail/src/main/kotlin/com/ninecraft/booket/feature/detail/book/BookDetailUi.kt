package com.ninecraft.booket.feature.detail.book

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.constants.BookStatus
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.book.component.BookItem
import com.ninecraft.booket.feature.detail.book.component.BookUpdateBottomSheet
import com.ninecraft.booket.feature.detail.book.component.CollectedSeeds
import com.ninecraft.booket.feature.detail.book.component.ReadingRecordsHeader
import com.ninecraft.booket.feature.detail.book.component.RecordItem
import com.ninecraft.booket.feature.detail.book.component.RecordSortBottomSheet
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun BookDetailUi(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    val bookUpdateBottomSheetState = rememberModalBottomSheetState()
    val recordSortBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    HandleBookDetailSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    ReedFullScreen(modifier = modifier) {
        ReedBackTopAppBar(
            title = "",
            onBackClick = {
                state.eventSink(BookDetailUiEvent.OnBackClick)
            },
        )
        BookDetailContent(state = state)
    }

    if (state.isBookUpdateBottomSheetVisible) {
        BookUpdateBottomSheet(
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnBookUpdateBottomSheetDismiss)
            },
            sheetState = bookUpdateBottomSheetState,
            onCloseButtonClick = {
                coroutineScope.launch {
                    bookUpdateBottomSheetState.hide()
                    state.eventSink(BookDetailUiEvent.OnBookUpdateBottomSheetDismiss)
                }
            },
            bookStatuses = BookStatus.entries.toTypedArray().toImmutableList(),
            currentBookStatus = state.currentBookStatus,
            onItemSelected = {
                state.eventSink(BookDetailUiEvent.OnBookStatusItemSelected(it))
            },
            onBookUpdateButtonClick = {
                state.eventSink(BookDetailUiEvent.OnBookStatusUpdateButtonClick)
            },
        )
    }

    if (state.isRecordSortBottomSheetVisible) {
        RecordSortBottomSheet(
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnRecordSortBottomSheetDismiss)
            },
            sheetState = recordSortBottomSheetState,
            onCloseButtonClick = {
                coroutineScope.launch {
                    recordSortBottomSheetState.hide()
                    state.eventSink(BookDetailUiEvent.OnRecordSortBottomSheetDismiss)
                }
            },
            recordSortItems = RecordSort.entries.toTypedArray().toImmutableList(),
            currentRecordSort = state.currentRecordSort,
            onItemSelected = {
                state.eventSink(BookDetailUiEvent.OnRecordSortItemSelected(it))
            },
        )
    }
}

@Composable
internal fun BookDetailContent(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    InfinityLazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        loadMore = {
            state.eventSink(BookDetailUiEvent.OnLoadMore)
        },
    ) {
        item {
            Column {
                BookItem(bookDetail = state.bookDetail)
                Spacer(Modifier.height(28.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ReedTheme.spacing.spacing5),
                ) {
                    ReedButton(
                        onClick = {
                            state.eventSink(BookDetailUiEvent.OnBookStatusButtonClick)
                        },
                        text = stringResource(
                            BookStatus.fromValue(state.bookDetail.userBookStatus)?.getDisplayNameRes()
                                ?: BookStatus.BEFORE_READING.getDisplayNameRes(),
                        ),
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.SECONDARY,
                        modifier = Modifier.widthIn(min = 98.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(designR.drawable.ic_chevron_down),
                                contentDescription = "Dropdown Icon",
                                modifier = Modifier.size(22.dp),
                                tint = ReedTheme.colors.contentPrimary,
                            )
                        },
                    )
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                    ReedButton(
                        onClick = {
                            state.eventSink(BookDetailUiEvent.OnRegisterRecordButtonClick)
                        },
                        text = stringResource(R.string.register_book_record),
                        sizeStyle = largeButtonStyle,
                        colorStyle = ReedButtonColorStyle.PRIMARY,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        item {
            if (state.hasEmotionData()) {
                CollectedSeeds(seedsStats = state.seedsStats)
            } else {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
            }

            ReedDivider()
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
            ) {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
                ReadingRecordsHeader(
                    readingRecords = state.readingRecords,
                    currentRecordSort = state.currentRecordSort,
                    onReadingRecordClick = {
                        state.eventSink(BookDetailUiEvent.OnRecordSortButtonClick)
                    },
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            }
        }

        if (state.readingRecords.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(324.dp)
                        .padding(horizontal = ReedTheme.spacing.spacing5),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.records_collection_empty),
                        color = ReedTheme.colors.contentSecondary,
                        textAlign = TextAlign.Center,
                        style = ReedTheme.typography.body1Medium,
                    )
                }
            }
        } else {
            items(
                count = state.readingRecords.size,
                key = { index -> state.readingRecords[index].id },
            ) { index ->
                val record = state.readingRecords[index]
                RecordItem(
                    quote = record.quote,
                    emotionTags = record.emotionTags.toImmutableList(),
                    pageNumber = record.pageNumber,
                    createdAt = record.createdAt,
                    modifier = Modifier
                        .padding(
                            start = ReedTheme.spacing.spacing5,
                            end = ReedTheme.spacing.spacing5,
                            bottom = ReedTheme.spacing.spacing3,
                        )
                        .clickable {
                            state.eventSink(BookDetailUiEvent.OnRecordItemClick(record.id))
                        },
                )
            }

            item {
                LoadStateFooter(
                    footerState = state.footerState,
                    onRetryClick = { state.eventSink(BookDetailUiEvent.OnLoadMore) },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BookDetailPreview() {
    ReedTheme {
        BookDetailUi(
            state = BookDetailUiState(
                eventSink = {},
            ),
        )
    }
}
