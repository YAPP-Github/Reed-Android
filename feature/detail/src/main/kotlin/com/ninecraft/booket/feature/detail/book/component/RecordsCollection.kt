package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.book.BookDetailUiState
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
            .height((192 * state.recordCollections.size).dp + 40.dp),
        contentPadding = PaddingValues(vertical = ReedTheme.spacing.spacing6),
        verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
        userScrollEnabled = false,
    ) {
        item {
            RecordsCollectionHeader(state = state)
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        }
        items(
            count = state.recordCollections.size,
            key = { index -> state.recordCollections[index].id },
        ) { index ->
            state.recordCollections[index].apply {
                RecordItem(
                    quote = quote,
                    emotionTags = emotionTags.toImmutableList(),
                    pageNumber = pageNumber,
                    createdAt = createdAt,
                )
            }
        }
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
