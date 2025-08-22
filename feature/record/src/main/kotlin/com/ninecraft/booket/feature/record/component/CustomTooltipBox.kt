package com.ninecraft.booket.feature.record.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.record.R

@Composable
internal fun CustomTooltipBox(
    @StringRes messageResId: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .shadow(ReedTheme.radius.xs, RoundedCornerShape(ReedTheme.radius.xs), clip = false)
                .background(
                    ReedTheme.colors.contentPrimary,
                    RoundedCornerShape(ReedTheme.radius.xs),
                )
                .padding(
                    horizontal = ReedTheme.spacing.spacing3,
                    vertical = ReedTheme.spacing.spacing2,
                ),
        ) {
            Text(
                text = stringResource(messageResId),
                color = ReedTheme.colors.contentInverse,
                style = ReedTheme.typography.label2Regular,
            )
        }
        Box(
            Modifier
                .padding(start = 2.dp)
                .size(ReedTheme.spacing.spacing3)
                .offset(
                    x = (-10).dp,
                )
                .graphicsLayer {
                    rotationZ = 45f
                    shadowElevation = 8.dp.toPx()
                    clip = true
                }
                .background(ReedTheme.colors.contentPrimary),
        )
    }
}

@ComponentPreview
@Composable
private fun CustomTooltipBoxPreview() {
    ReedTheme {
        CustomTooltipBox(messageResId = R.string.scan_tooltip_message)
    }
}
