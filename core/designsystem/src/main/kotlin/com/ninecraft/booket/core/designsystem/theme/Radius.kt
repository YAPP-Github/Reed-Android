package com.ninecraft.booket.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ReedRadius(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 28.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp,
    val full: Dp = 1000.dp,
)
