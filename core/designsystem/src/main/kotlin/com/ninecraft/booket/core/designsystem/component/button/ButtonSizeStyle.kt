package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

data class ButtonSizeStyle(
    val paddingValues: PaddingValues,
    val radius: Dp = 0.dp,
    val textStyle: TextStyle,
    val iconSpacing: Dp = 0.dp,
    val iconSize: Dp = 24.dp,
)

val largeButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing5,
            vertical = 14.dp,
        ),
        radius = ReedTheme.radius.sm,
        textStyle = ReedTheme.typography.body1Medium,
        iconSpacing = ReedTheme.spacing.spacing2,
        iconSize = 24.dp,
    )

val mediumButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing4,
            vertical = ReedTheme.spacing.spacing3,
        ),
        radius = ReedTheme.radius.sm,
        textStyle = ReedTheme.typography.label1Medium,
        iconSpacing = ReedTheme.spacing.spacing1,
        iconSize = 22.dp,
    )

val smallButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing3,
            vertical = ReedTheme.spacing.spacing2,
        ),
        radius = ReedTheme.radius.xs,
        textStyle = ReedTheme.typography.label1Medium,
        iconSpacing = ReedTheme.spacing.spacing1,
        iconSize = 18.dp,
    )

val largeRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing5,
            vertical = ReedTheme.spacing.spacing3,
        ),
        radius = ReedTheme.radius.full,
        textStyle = ReedTheme.typography.body1Medium,
        iconSpacing = ReedTheme.spacing.spacing2,
        iconSize = 24.dp,
    )

val mediumRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing4,
            vertical = ReedTheme.spacing.spacing3,
        ),
        radius = ReedTheme.radius.full,
        textStyle = ReedTheme.typography.label1Medium,
        iconSpacing = ReedTheme.spacing.spacing1,
        iconSize = 22.dp,
    )

val smallRoundedButtonStyle: ButtonSizeStyle
    @Composable get() = ButtonSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing3,
            vertical = ReedTheme.spacing.spacing2,
        ),
        radius = ReedTheme.radius.full,
        textStyle = ReedTheme.typography.label1Medium,
        iconSpacing = ReedTheme.spacing.spacing1,
        iconSize = 18.dp,
    )
