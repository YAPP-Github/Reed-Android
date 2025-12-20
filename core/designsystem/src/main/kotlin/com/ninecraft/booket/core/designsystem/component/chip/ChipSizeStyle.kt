package com.ninecraft.booket.core.designsystem.component.chip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

data class ChipSizeStyle(
    val paddingValues: PaddingValues,
    val textStyle: TextStyle,
)

val mediumChipStyle: ChipSizeStyle
    @Composable get() = ChipSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing3,
            vertical = ReedTheme.spacing.spacing2,
        ),
        textStyle = ReedTheme.typography.body2Medium,
    )

val smallChipStyle: ChipSizeStyle
    @Composable get() = ChipSizeStyle(
        paddingValues = PaddingValues(
            horizontal = ReedTheme.spacing.spacing3,
            vertical = ReedTheme.spacing.spacing15,
        ),
        textStyle = ReedTheme.typography.label1Medium,
    )
