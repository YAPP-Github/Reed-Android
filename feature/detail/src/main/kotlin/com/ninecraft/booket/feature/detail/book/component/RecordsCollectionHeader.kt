package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.book.BookDetailUiEvent
import com.ninecraft.booket.feature.detail.book.BookDetailUiState
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun RecordsCollectionHeader(
    state: BookDetailUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                color = ReedTheme.colors.contentBrand,
                style = ReedTheme.typography.headline2SemiBold,
            )
        }
        Row(
            modifier = Modifier.clickable {
                state.eventSink(BookDetailUiEvent.OnRecordSortButtonClick)
            },
        ) {
            Text(
                text = stringResource(state.currentRecordSort.getDisplayNameRes()),
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.label1Medium,
            )
            Icon(
                imageVector = ImageVector.vectorResource(designR.drawable.ic_chevron_down),
                contentDescription = "Dropdown Icon",
                tint = ReedTheme.colors.contentSecondary,
            )
        }
    }
}
