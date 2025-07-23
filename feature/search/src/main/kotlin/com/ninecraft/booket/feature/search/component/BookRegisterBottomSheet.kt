package com.ninecraft.booket.feature.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.search.BookStatus
import com.ninecraft.booket.feature.search.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRegisterBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onCloseButtonClick: () -> Unit,
    bookStatuses: ImmutableList<BookStatus>,
    currentBookStatus: BookStatus?,
    onItemSelected: (BookStatus) -> Unit,
    onBookRegisterButtonClick: () -> Unit,
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
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.book_register_title),
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
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                bookStatuses.forEach { item ->
                    BookStatusItem(
                        item = item,
                        selected = item == currentBookStatus,
                        onClick = {
                            if (item != currentBookStatus) {
                                onItemSelected(item)
                            }
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            ReedButton(
                onClick = {
                    onBookRegisterButtonClick()
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth(),
                enabled = currentBookStatus != null,
                text = stringResource(R.string.book_register_ok),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        }
    }
}

@Composable
fun RowScope.BookStatusItem(
    item: BookStatus,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .weight(1f)
            .clip(RoundedCornerShape(ReedTheme.radius.sm))
            .background(if (selected) ReedTheme.colors.bgTertiary else ReedTheme.colors.bgSecondary)
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(vertical = ReedTheme.spacing.spacing3),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(item.getDisplayNameRes()),
            color = if (selected) ReedTheme.colors.contentBrand else ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun BookRegisterBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        BookRegisterBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            onCloseButtonClick = {},
            bookStatuses = BookStatus.entries.toImmutableList(),
            currentBookStatus = null,
            onItemSelected = {},
            onBookRegisterButtonClick = {},
        )
    }
}
