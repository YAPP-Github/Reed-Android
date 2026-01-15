package com.ninecraft.booket.feature.record.ocr.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.Green100
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun ImageProcessingLoader(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    // 점들의 애니메이션 상태 리스트 (0ms, 300ms, 600ms 지연)
    val dotAnimations = listOf(0, 1, 2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0.0f at 0 using LinearOutSlowInEasing   // 시작
                    1.0f at 300 using FastOutLinearInEasing // 최고점
                    0.0f at 600 using LinearOutSlowInEasing // 바닥 도착
                    0.0f at 1200                            // 대기
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * 200), // 순차적 시작 지연
            ),
            label = "dot_y_offset",
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        dotAnimations.forEachIndexed { index, animValue ->
            Box(
                modifier = Modifier
                    .size(ReedTheme.spacing.spacing3)
                    // 애니메이션 값에 따라 y축 좌표 이동
                    .graphicsLayer {
                        translationY = -animValue.value * 12.dp.toPx()
                    }
                    .background(
                        color = if (animValue.value > 0f) ReedTheme.colors.contentBrand else Green100,
                        shape = CircleShape,
                    ),
            )

            if (index < 2) {
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing3))
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ImageProcessingLoaderPreview() {
    ReedTheme {
        ImageProcessingLoader()
    }
}
