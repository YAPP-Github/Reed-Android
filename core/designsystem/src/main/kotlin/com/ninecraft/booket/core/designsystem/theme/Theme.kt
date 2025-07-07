package com.ninecraft.booket.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalColorScheme = staticCompositionLocalOf { ReedColorScheme() }
private val LocalTypography = staticCompositionLocalOf { ReedTypography() }
private val LocalSpacing = staticCompositionLocalOf { ReedSpacing() }
private val LocalRadius = staticCompositionLocalOf { ReedRadius() }
private val LocalBorder = staticCompositionLocalOf { ReedBorder() }

@Composable
fun ReedTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        content = content,
    )
}

object ReedTheme {
    val colors: ReedColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: ReedTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val spacing: ReedSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val radius: ReedRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalRadius.current

    val border: ReedBorder
        @Composable
        @ReadOnlyComposable
        get() = LocalBorder.current
}
