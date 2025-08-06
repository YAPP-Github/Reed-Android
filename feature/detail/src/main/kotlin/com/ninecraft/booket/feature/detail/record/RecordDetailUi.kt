package com.ninecraft.booket.feature.detail.record

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.component.ReedDivider
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.RecordDetailModel
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.record.component.QuoteBox
import com.ninecraft.booket.feature.detail.record.component.ReviewBox
import com.ninecraft.booket.feature.screens.RecordDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(RecordDetailScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordDetailUi(
    state: RecordDetailUiState,
    modifier: Modifier = Modifier,
) {
    HandleRecordDetailSideEffects(
        state = state,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ReedTopAppBar(
                title = stringResource(R.string.review_detail_title),
                startIconRes = designR.drawable.ic_close,
                startIconDescription = "Close Icon",
                startIconOnClick = {
                    state.eventSink(RecordDetailUiEvent.OnCloseClicked)
                },
            )
        },
        containerColor = White,
    ) { innerPadding ->
        ReviewDetailContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@Composable
private fun ReviewDetailContent(
    state: RecordDetailUiState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ReedTheme.spacing.spacing5,
                    vertical = ReedTheme.spacing.spacing4,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NetworkImage(
                imageUrl = state.recordDetailInfo.bookCoverImageUrl,
                contentDescription = "Book CoverImage",
                modifier = Modifier
                    .padding(end = ReedTheme.spacing.spacing4)
                    .width(46.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(size = ReedTheme.radius.xs)),
                placeholder = painterResource(designR.drawable.ic_placeholder),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.recordDetailInfo.bookTitle,
                    color = ReedTheme.colors.contentPrimary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.body1SemiBold,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val authorMaxWidth = maxWidth * 0.7f

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = state.recordDetailInfo.author,
                            color = ReedTheme.colors.contentTertiary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = ReedTheme.typography.label1Medium,
                            modifier = Modifier.widthIn(max = authorMaxWidth),
                        )
                        Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                        VerticalDivider(
                            modifier = Modifier.height(14.dp),
                            thickness = 1.dp,
                            color = ReedTheme.colors.contentTertiary,
                        )
                        Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                        Text(
                            text = state.recordDetailInfo.bookPublisher,
                            color = ReedTheme.colors.contentTertiary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = ReedTheme.typography.label1Medium,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                    }
                }
            }
        }
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
            QuoteBox(
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
            ReviewBox(
                emotion = state.recordDetailInfo.emotionTags.getOrNull(0) ?: "",
                createdAt = state.recordDetailInfo.createdAt,
                review = state.recordDetailInfo.review,
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
