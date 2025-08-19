package com.ninecraft.booket.feature.detail.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.RecordDetailModel
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.core.ui.component.ReedErrorUi
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.record.component.BookItem
import com.ninecraft.booket.feature.detail.record.component.QuoteItem
import com.ninecraft.booket.feature.detail.record.component.RecordMenuBottomSheet
import com.ninecraft.booket.feature.detail.record.component.ReviewItem
import com.ninecraft.booket.feature.screens.RecordDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(RecordDetailScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordDetailUi(
    state: RecordDetailUiState,
    modifier: Modifier = Modifier,
) {
    val recordMenuBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    HandleRecordDetailSideEffects(
        state = state,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            ReedTopAppBar(
                title = stringResource(R.string.review_detail_title),
                startIconRes = designR.drawable.ic_close,
                startIconDescription = "Close Icon",
                startIconOnClick = {
                    state.eventSink(RecordDetailUiEvent.OnCloseClick)
                },
                endIconRes = designR.drawable.ic_more_vertical,
                endIconDescription = "More Vertical Icon",
                endIconOnClick = {
                    state.eventSink(RecordDetailUiEvent.OnRecordMenuClick)
                },
            )
            RecordDetailContent(state = state)
        }
    }

    if (state.isRecordMenuBottomSheetVisible) {
        RecordMenuBottomSheet(
            onDismissRequest = {
                state.eventSink(RecordDetailUiEvent.OnRecordMenuBottomSheetDismiss)
            },
            sheetState = recordMenuBottomSheetState,
            onShareRecordClick = {},
            onEditRecordClick = {
                coroutineScope.launch {
                    recordMenuBottomSheetState.hide()
                    state.eventSink(RecordDetailUiEvent.OnEditRecordClick)
                }
            },
            onDeleteRecordClick = {
                state.eventSink(RecordDetailUiEvent.OnDeleteRecordClick)
            },
        )
    }

    if (state.isRecordDeleteDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.record_delete_dialog_title),
            confirmButtonText = stringResource(R.string.record_delete_dialog_delete),
            onConfirmRequest = {
                state.eventSink(RecordDetailUiEvent.OnDelete)
            },
            dismissButtonText = stringResource(R.string.record_delete_dialog_cancel),
            onDismissRequest = {
                state.eventSink(RecordDetailUiEvent.OnRecordDeleteDialogDismiss)
            },
        )
    }
}

@Composable
private fun RecordDetailContent(
    state: RecordDetailUiState,
    modifier: Modifier = Modifier,
) {
    when (state.uiState) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = ReedTheme.colors.contentBrand)
            }
        }

        is UiState.Success -> {
            BookItem(
                imageUrl = state.recordDetailInfo.bookCoverImageUrl,
                bookTitle = state.recordDetailInfo.bookTitle,
                author = state.recordDetailInfo.author,
                publisher = state.recordDetailInfo.bookPublisher,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            ReedDivider()
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ReedTheme.spacing.spacing5),
            ) {
                Text(
                    text = stringResource(R.string.review_detail_quote_label),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.body1Medium,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
                QuoteItem(
                    quote = state.recordDetailInfo.quote,
                    page = state.recordDetailInfo.pageNumber,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
                Text(
                    text = stringResource(R.string.review_detail_impression_label),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.body1Medium,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
                ReviewItem(
                    emotion = state.recordDetailInfo.emotionTags.getOrNull(0) ?: "",
                    createdAt = state.recordDetailInfo.createdAt,
                    review = state.recordDetailInfo.review,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            }
        }

        is UiState.Error -> {
            ReedErrorUi(
                exception = state.uiState.exception,
                onRetryClick = { },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReviewDetailPreview() {
    ReedTheme {
        RecordDetailUi(
            state = RecordDetailUiState(
                uiState = UiState.Success,
                recordDetailInfo = RecordDetailModel(
                    id = "",
                    userBookId = "",
                    pageNumber = 90,
                    quote = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                    review = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다",
                    emotionTags = listOf("따뜻함"),
                    createdAt = "2023.10.10",
                    updatedAt = "",
                    bookTitle = "여름은 오래 그곳에 남아",
                    bookPublisher = "비채 비채 비채 비채",
                    bookCoverImageUrl = "",
                    author = "미쓰이에 마사시, 미쓰이에 마사시, 미쓰이에 마사시, 미쓰이에 마사시",
                ),
                eventSink = {},
            ),
        )
    }
}
