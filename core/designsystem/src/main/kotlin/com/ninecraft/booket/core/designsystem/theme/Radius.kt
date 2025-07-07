package com.ninecraft.booket.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ReedRadius(
    val radiusNone: Dp = 0.dp,
    val radiusXs: Dp = 4.dp,
    val radiusSm: Dp = 8.dp,
    val radiusMd: Dp = 12.dp,
    val radiusLg: Dp = 16.dp,
    val radiusXl: Dp = 28.dp,
    val radius2xl: Dp = 32.dp,
    val radius3xl: Dp = 48.dp,
    val radiusFull: Dp = 1000.dp,
)

