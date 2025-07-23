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
import com.ninecraft.booket.feature.library.LibraryFilterOption
import com.ninecraft.booket.feature.library.LibraryFilterChip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FilterChipGroup(
    filterList: ImmutableList<LibraryFilterChip>,
    selectedChipOption: LibraryFilterOption,
    onChipClick: (LibraryFilterOption) -> Unit,
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
                option = item.option,
                count = item.count,
                isSelected = selectedChipOption == item.option,
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
    val filterList = LibraryFilterOption.entries.map { LibraryFilterChip(option = it, count = 0) }.toPersistentList()
    ReedTheme {
        FilterChipGroup(
            filterList = filterList,
            selectedChipOption = LibraryFilterOption.TOTAL,
            onChipClick = {},
        )
    }
}
