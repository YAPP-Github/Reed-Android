package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.book.RecordSort
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordSortBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onCloseButtonClick: () -> Unit,
    recordSortItems: ImmutableList<RecordSort>,
    currentRecordSort: RecordSort,
    onItemSelected: (RecordSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier
                .padding(top = ReedTheme.spacing.spacing5),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ReedTheme.spacing.spacing5),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.record_sort_title),
                    color = ReedTheme.colors.contentPrimary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.heading2SemiBold,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_close),
                    contentDescription = "Close Icon",
                    modifier = Modifier.clickable {
                        onCloseButtonClick()
                    },
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                recordSortItems.forEachIndexed { index, item ->
                    RecordSortItem(
                        item = item,
                        selected = item == currentRecordSort,
                        onClick = {
                            if (item != currentRecordSort) {
                                onItemSelected(item)
                            }
                        },
                        modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
                    )

                    if (index < recordSortItems.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = ReedTheme.colors.dividerSm,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecordSortItem(
    item: RecordSort,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(
                horizontal = ReedTheme.spacing.spacing1,
                vertical = ReedTheme.spacing.spacing4,
            ),
    ) {
        Text(
            text = stringResource(item.getDisplayNameRes()),
            color = if (selected) ReedTheme.colors.contentBrand else ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.body1Medium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun RecordSortBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        RecordSortBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            onCloseButtonClick = {},
            recordSortItems = RecordSort.entries.toImmutableList(),
            currentRecordSort = RecordSort.PAGE_NUMBER_ASC,
            onItemSelected = {},
        )
    }
}
