package com.ninecraft.booket.feature.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.library.BookStatus
import com.ninecraft.booket.feature.library.FilterChipState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FilterChipGroup(
    filterList: ImmutableList<FilterChipState>,
    onChipClick: (BookStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing3,
                end = ReedTheme.spacing.spacing5,
                bottom = ReedTheme.spacing.spacing3,
            ),
        horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        filterList.forEach { item ->
            FilterChip(
                status = item.title,
                count = item.count,
                isSelected = item.isSelected,
                onChipClick = { status ->
                    onChipClick(status)
                },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun FilterChipGroupPreview() {
    val filterList = persistentListOf(
        FilterChipState(
            title = BookStatus.TOTAL,
            count = 10,
            isSelected = true,
        ),
        FilterChipState(
            title = BookStatus.BEFORE_READING,
            count = 15,
            isSelected = false,
        ),
        FilterChipState(
            title = BookStatus.READING,
            count = 2,
            isSelected = false,
        ),
        FilterChipState(
            title = BookStatus.COMPLETED,
            count = 5,
            isSelected = false,
        ),
    )
    ReedTheme {
        FilterChipGroup(
            filterList = filterList,
            onChipClick = {},
        )
    }
}
