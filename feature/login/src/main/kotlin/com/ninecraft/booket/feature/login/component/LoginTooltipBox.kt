package com.ninecraft.booket.feature.login.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.login.R

private val TriangleShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            // 왼쪽 위
            moveTo(0f, 0f)
            // 오른쪽 위
            lineTo(size.width, 0f)
            // 중앙 아래 (뾰족한 부분)
            lineTo(size.width / 2, size.height)
            // 닫기
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
internal fun LoginTooltipBox(
    @StringRes messageResId: Int,
) {
    Column(horizontalAlignment = Alignment.Start) {
        Box(
            Modifier
                .shadow(ReedTheme.radius.xs, RoundedCornerShape(ReedTheme.radius.xs), clip = false)
                .clip(RoundedCornerShape(ReedTheme.radius.xs))
                .background(ReedTheme.colors.contentBrand)
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
                .width(ReedTheme.spacing.spacing3)
                .height(ReedTheme.spacing.spacing3 / 2)
                .offset {
                    IntOffset(
                        x = 14.dp.roundToPx(),
                        y = 0,
                    )
                }
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                    shape = TriangleShape
                    clip = true
                }
                .background(ReedTheme.colors.contentBrand),
        )
    }
}

@ComponentPreview
@Composable
private fun RecordTooltipBoxPreview() {
    ReedTheme {
        LoginTooltipBox(messageResId = R.string.recent_login)
    }
}
