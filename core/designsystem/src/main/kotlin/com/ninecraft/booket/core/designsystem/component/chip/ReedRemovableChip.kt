package com.ninecraft.booket.core.designsystem.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun ReedRemovableChip(
    label: String,
    chipSizeStyle: ChipSizeStyle,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerShape = RoundedCornerShape(ReedTheme.radius.full)

    Row(
        modifier = modifier
            .clip(cornerShape)
            .background(color = ReedTheme.colors.bgTertiary)
            .border(
                width = 1.dp,
                color = ReedTheme.colors.borderBrand,
                shape = cornerShape,
            )
            .padding(chipSizeStyle.paddingValues),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = ReedTheme.colors.contentBrand,
            style = chipSizeStyle.textStyle,
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            contentDescription = "Icon Close",
            tint = ReedTheme.colors.contentBrand,
            modifier = Modifier
                .size(14.dp)
                .noRippleClickable {
                    onRemove()
                },
        )
    }
}

@ComponentPreview
@Composable
private fun ReedRemovableChipPreview() {
    ReedTheme {
        ReedRemovableChip(
            label = "text",
            chipSizeStyle = mediumChipStyle,
            onRemove = {}
        )
    }
}
