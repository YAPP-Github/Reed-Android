package com.ninecraft.booket.feature.detail.book

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.ninecraft.booket.core.common.extensions.toErrorType
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.mediumButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.InfinityLazyColumn
import com.ninecraft.booket.core.ui.component.LoadStateFooter
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.core.ui.component.ReedErrorUi
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.book.component.BookItem
import com.ninecraft.booket.feature.detail.book.component.BookUpdateBottomSheet
import com.ninecraft.booket.feature.detail.book.component.CollectedSeeds
import com.ninecraft.booket.feature.detail.book.component.DetailMenuBottomSheet
import com.ninecraft.booket.feature.detail.book.component.ReadingRecordsHeader
import com.ninecraft.booket.feature.detail.book.component.RecordItem
import com.ninecraft.booket.feature.detail.book.component.RecordSortBottomSheet
import com.ninecraft.booket.feature.detail.record.component.RecordMenuBottomSheet
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@TraceRecomposition
@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(BookDetailScreen::class, AppScope::class)
@Composable
internal fun BookDetailUi(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    val bookUpdateBottomSheetState = rememberModalBottomSheetState()
    val recordSortBottomSheetState = rememberModalBottomSheetState()
    val recordMenuBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    HandleBookDetailSideEffects(
        state = state,
        eventSink = state.eventSink,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        BookDetailContent(
            state = state,
            innerPadding = innerPadding,
        )
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
            selectedBookStatus = state.selectedBookStatus,
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

    if (state.isRecordMenuBottomSheetVisible) {
        RecordMenuBottomSheet(
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnRecordMenuBottomSheetDismiss)
            },
            sheetState = recordMenuBottomSheetState,
            onShareRecordClick = {
                coroutineScope.launch {
                    recordMenuBottomSheetState.hide()
                    state.eventSink(BookDetailUiEvent.OnShareRecordClick)
                }
            },
            onEditRecordClick = {
                coroutineScope.launch {
                    recordMenuBottomSheetState.hide()
                    state.eventSink(BookDetailUiEvent.OnEditRecordClick)
                }
            },
            onDeleteRecordClick = {
                state.eventSink(BookDetailUiEvent.OnDeleteRecordClick)
            },
        )
    }

    if (state.isRecordDeleteDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.record_delete_dialog_title),
            confirmButtonText = stringResource(R.string.record_delete_dialog_delete),
            onConfirmRequest = {
                state.eventSink(BookDetailUiEvent.OnDeleteRecord)
            },
            dismissButtonText = stringResource(R.string.record_delete_dialog_cancel),
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnRecordDeleteDialogDismiss)
            },
        )
    }

    if (state.isDetailMenuBottomSheetVisible) {
        DetailMenuBottomSheet(
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnDetailMenuBottomSheetDismiss)
            },
            sheetState = recordMenuBottomSheetState,
            onDeleteBookClick = {
                state.eventSink(BookDetailUiEvent.OnDeleteBookClick)
            },
        )
    }

    if (state.isBookDeleteDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.record_delete_dialog_title),
            confirmButtonText = stringResource(R.string.record_delete_dialog_delete),
            onConfirmRequest = {
                state.eventSink(BookDetailUiEvent.OnDeleteBook)
            },
            dismissButtonText = stringResource(R.string.record_delete_dialog_cancel),
            onDismissRequest = {
                state.eventSink(BookDetailUiEvent.OnDeleteDialogDismiss)
            },
        )
    }
}

@TraceRecomposition
@Composable
internal fun BookDetailContent(
    state: BookDetailUiState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    when (state.uiState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            ReedLoadingIndicator()
        }

        is UiState.Success -> {
            InfinityLazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = lazyListState,
                loadMore = {
                    state.eventSink(BookDetailUiEvent.OnLoadMore)
                },
            ) {
                item {
                    ReedTopAppBar(
                        startIconRes = designR.drawable.ic_chevron_left,
                        startIconOnClick = {
                            state.eventSink(BookDetailUiEvent.OnBackClick)
                        },
                        endIconRes = designR.drawable.ic_more_vertical,
                        endIconDescription = "More Vertical Icon",
                        endIconOnClick = {
                            state.eventSink(BookDetailUiEvent.OnDetailMenuClick)
                        },
                    )
                }

                item {
                    Column {
                        BookItem(bookDetail = state.bookDetail)
                        Spacer(Modifier.height(ReedTheme.spacing.spacing5))
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
                                sizeStyle = mediumButtonStyle,
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
                                sizeStyle = mediumButtonStyle,
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
                        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
                        ReadingRecordsHeader(
                            totalCount = state.readingRecordsTotalCount,
                            currentRecordSort = state.currentRecordSort,
                            onReadingRecordClick = {
                                state.eventSink(BookDetailUiEvent.OnRecordSortButtonClick)
                            },
                        )
                        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
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
                            recordInfo = record,
                            onRecordClick = {
                                state.eventSink(BookDetailUiEvent.OnRecordItemClick(record.id))
                            },
                            onRecordMenuClick = { recordInfo ->
                                state.eventSink(BookDetailUiEvent.OnRecordMenuClick(recordInfo))
                            },
                            modifier = Modifier
                                .padding(
                                    start = ReedTheme.spacing.spacing5,
                                    end = ReedTheme.spacing.spacing5,
                                    bottom = ReedTheme.spacing.spacing3,
                                ),
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

        is UiState.Error -> {
            ReedErrorUi(
                errorType = state.uiState.exception.toErrorType(),
                onRetryClick = { state.eventSink(BookDetailUiEvent.OnRetryClick) },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BookDetailPreview() {
    ReedTheme {
        BookDetailUi(
            state = BookDetailUiState(
                uiState = UiState.Success,
                bookDetail = BookDetailModel(
                    title = "데미안",
                    author = "헤르만 헤세",
                    publisher = "민음사",
                    pubDate = "2023-01-01",
                    coverImageUrl = "",
                ),
                eventSink = {},
            ),
        )
    }
}

@ComponentPreview
@Composable
private fun BookDetailWithSeedsPreview() {
    ReedTheme {
        BookDetailUi(
            state = BookDetailUiState(
                uiState = UiState.Success,
                bookDetail = BookDetailModel(
                    title = "데미안",
                    author = "헤르만 헤세",
                    publisher = "민음사",
                    pubDate = "2023-01-01",
                    coverImageUrl = "",
                ),
                seedsStats = persistentListOf(
                    EmotionModel(name = Emotion.WARM, count = 5),
                    EmotionModel(name = Emotion.JOY, count = 3),
                    EmotionModel(name = Emotion.SAD, count = 2),
                    EmotionModel(name = Emotion.INSIGHT, count = 7),
                ),
                readingRecords = persistentListOf(
                    ReadingRecordModel(
                        id = "1",
                        pageNumber = 42,
                        quote = "새는 알에서 나오려고 투쟁한다. 알은 세계이다.",
                        review = "정말 인상 깊은 구절이었다.",
                        emotionTags = listOf("깨달음", "따뜻함"),
                        createdAt = "2024-01-15T10:30:00.000000",
                    ),
                    ReadingRecordModel(
                        id = "2",
                        pageNumber = 78,
                        quote = "나는 더 이상 꿈을 꾸지 않으려 했다.",
                        review = "성장통을 느끼는 부분",
                        emotionTags = listOf("슬픔"),
                        createdAt = "2024-01-20T14:20:00.000000",
                    ),
                    ReadingRecordModel(
                        id = "3",
                        pageNumber = 156,
                        quote = "운명과 성향은 같은 개념의 두 이름이다.",
                        review = "내 삶을 돌아보게 되었다.",
                        emotionTags = listOf("깨달음", "즐거움"),
                        createdAt = "2024-01-25T09:15:00.000000",
                    ),
                ),
                readingRecordsTotalCount = 3,
                eventSink = {},
            ),
        )
    }
}
