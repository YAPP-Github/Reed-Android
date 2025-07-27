package com.ninecraft.booket.feature.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R

@Composable
fun ImpressionGuideBox(
    onClick: () -> Unit,
    impressionText: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val bgColor = if (isSelected) ReedTheme.colors.bgTertiary else White
    val borderColor = if (isSelected) ReedTheme.colors.borderBrand else ReedTheme.colors.borderPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .clip(RoundedCornerShape(ReedTheme.radius.sm))
            .clickableSingle {
                onClick()
            }
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing4,
            ),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(R.string.impression_guide_blank),
                color = Color(0xFFD6D6D6),
                style = ReedTheme.typography.label1SemiBold,
            )
            Text(
                text = impressionText,
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.label1SemiBold,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ImpressionGuideBoxPreview() {
    ReedTheme {
        ImpressionGuideBox(
            onClick = {},
            impressionText = "에서 위로 받았다",
        )
    }
}
