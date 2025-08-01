package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.book.BookDetailUiEvent
import com.ninecraft.booket.feature.detail.book.BookDetailUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun RecordsCollection(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReedTheme.spacing.spacing5)
            .then(
                if (state.recordCollections.isEmpty()) {
                    Modifier.height(400.dp)
                } else {
                    // contentPadding + Header + (RecordItem + padding) * size
                    Modifier.height(36.dp + 40.dp + (192 * state.recordCollections.size).dp)
                },
            ),
        contentPadding = PaddingValues(
            top = ReedTheme.spacing.spacing6,
            bottom = ReedTheme.spacing.spacing3,
        ),
        verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
        userScrollEnabled = false,
    ) {
        item {
            RecordsCollectionHeader(state = state)
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        }

        if (state.recordCollections.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(324.dp), // 400.dp - (contentPadding + Header)
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
                count = state.recordCollections.size,
                key = { index -> state.recordCollections[index].id },
            ) { index ->
                val record = state.recordCollections[index]
                RecordItem(
                    quote = record.quote,
                    emotionTags = record.emotionTags.toImmutableList(),
                    pageNumber = record.pageNumber,
                    createdAt = record.createdAt,
                    modifier = Modifier.clickable {
                        state.eventSink(BookDetailUiEvent.OnRecordItemClick(record.id))
                    },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun RecordsCollectionEmptyPreview() {
    ReedTheme {
        RecordsCollection(
            state = BookDetailUiState(
                recordCollections = persistentListOf(),
                eventSink = {},
            ),
        )
    }
}

@ComponentPreview
@Composable
private fun RecordsCollectionPreview() {
    ReedTheme {
        RecordsCollection(
            state = BookDetailUiState(
                eventSink = {},
            ),
        )
    }
}
