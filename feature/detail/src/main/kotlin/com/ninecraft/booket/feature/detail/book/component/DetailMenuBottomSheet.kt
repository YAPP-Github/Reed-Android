package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailMenuBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onDeleteBookClick: () -> Unit,
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
            DetailMenuItem(
                iconResId = designR.drawable.ic_trash,
                iconDescription = "Trash Icon",
                label = stringResource(R.string.book_delete),
                color = ReedTheme.colors.contentError,
                onClick = { onDeleteBookClick() },
            )
        }
    }
}

@Composable
private fun DetailMenuItem(
    iconResId: Int,
    iconDescription: String,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onClick()
            }
            .padding(
                vertical = ReedTheme.spacing.spacing5,
                horizontal = ReedTheme.spacing.spacing6,
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconResId),
            contentDescription = iconDescription,
            tint = color,
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing3))
        Text(
            text = label,
            color = color,
            style = ReedTheme.typography.body1Medium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun ChoiceBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )

    DetailMenuBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        onDeleteBookClick = {},
    )
}
