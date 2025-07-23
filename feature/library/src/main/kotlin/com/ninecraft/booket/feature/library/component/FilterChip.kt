package com.ninecraft.booket.feature.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.library.LibraryFilterOption

@Composable
fun FilterChip(
    option: LibraryFilterOption,
    count: Int,
    isSelected: Boolean,
    onChipClick: (LibraryFilterOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    val chipColor = if (isSelected) ReedTheme.colors.bgPrimary else ReedTheme.colors.basePrimary
    val textColor = if (isSelected) White else ReedTheme.colors.contentSecondary

    Box(
        modifier = modifier
            .background(
                color = chipColor,
                shape = RoundedCornerShape(ReedTheme.radius.full),
            )
            .clip(shape = RoundedCornerShape(ReedTheme.radius.full))
            .noRippleClickable {
                onChipClick(option)
            }
            .then(
                if (isSelected) {
                    modifier
                } else {
                    modifier.border(
                        width = 1.dp,
                        color = ReedTheme.colors.borderPrimary,
                        shape = RoundedCornerShape(ReedTheme.radius.full),
                    )
                },
            )
            .padding(horizontal = ReedTheme.spacing.spacing3, vertical = ReedTheme.spacing.spacing2),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(option.getDisplayNameRes()),
                color = textColor,
                style = ReedTheme.typography.label1SemiBold,
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
            Text(
                text = "$count",
                color = textColor,
                style = ReedTheme.typography.label1SemiBold,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ChipPreview() {
    ReedTheme {
        FilterChip(
            option = LibraryFilterOption.TOTAL,
            count = 10,
            isSelected = true,
            onChipClick = {},
        )
    }
}
