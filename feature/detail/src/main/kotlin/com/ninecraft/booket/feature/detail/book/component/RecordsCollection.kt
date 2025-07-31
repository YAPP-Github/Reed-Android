package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.core.designsystem.R as designR
import com.ninecraft.booket.feature.detail.book.BookDetailUiState
import kotlinx.collections.immutable.toImmutableList

// TODO 필요한 파라미터만 선언하여 사용
@Composable
internal fun RecordsCollection(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReedTheme.spacing.spacing5),
        contentPadding = PaddingValues(vertical = ReedTheme.spacing.spacing6),
        verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.record_collection),
                        color = ReedTheme.colors.contentPrimary,
                        style = ReedTheme.typography.headline2SemiBold,
                    )
                    Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
                    Text(
                        text = "${state.recordCollections.size}",
                        color = ReedTheme.colors.contentPrimary,
                        style = ReedTheme.typography.headline2SemiBold,
                    )
                }
                Row {
                    Text(
                        text = stringResource(state.currentRecordSort.getDisplayNameRes()),
                        color = ReedTheme.colors.contentSecondary,
                        style = ReedTheme.typography.label1Medium,
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(designR.drawable.ic_chevron_down),
                        contentDescription = "Dropdown Icon",
                    )
                }
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        }
        items(
            count = state.recordCollections.size,
            key = { index -> state.recordCollections[index].id },
        ) { index ->
            RecordItem(
                quote = state.recordCollections[index].quote,
                emotionTags = state.recordCollections[index].emotionTags.toImmutableList(),
                pageNumber = state.recordCollections[index].pageNumber,
                createdAt = state.recordCollections[index].createdAt,
            )
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
