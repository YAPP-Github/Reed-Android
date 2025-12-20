package com.ninecraft.booket.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun ReedChip(
    label: String,
    chipSizeStyle: ChipSizeStyle,
    selected: Boolean,
    showCloseIcon: Boolean = false,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerShape = RoundedCornerShape(ReedTheme.radius.full)
    val backgroundColor = if (selected) ReedTheme.colors.bgTertiary else ReedTheme.colors.basePrimary
    val borderColor = if (selected) ReedTheme.colors.borderBrand else ReedTheme.colors.borderPrimary
    val textColor = if (selected) ReedTheme.colors.contentBrand else ReedTheme.colors.contentSecondary

    Row(
        modifier = modifier
            .clip(cornerShape)
            .background(color = backgroundColor)
            .noRippleClickable {
                onClick(!selected)
            }
            .border(
                width = 1.dp,
                color = borderColor,
                shape = cornerShape,
            )
            .padding(chipSizeStyle.paddingValues),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = textColor,
            style = chipSizeStyle.textStyle,
        )
        if (showCloseIcon) {
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "Icon Close",
                tint = textColor,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReedChipPreview() {
    ReedTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing1),
        ) {
            ReedChip(
                label = "text",
                chipSizeStyle = mediumChipStyle,
                showCloseIcon = true,
                selected = false,
                onClick = {},
            )
            ReedChip(
                label = "text",
                chipSizeStyle = mediumChipStyle,
                showCloseIcon = true,
                selected = true,
                onClick = {},
            )
            ReedChip(
                label = "text",
                chipSizeStyle = smallChipStyle,
                showCloseIcon = true,
                selected = false,
                onClick = {},
            )
            ReedChip(
                label = "text",
                chipSizeStyle = smallChipStyle,
                showCloseIcon = true,
                selected = true,
                onClick = {},
            )
        }
    }
}
