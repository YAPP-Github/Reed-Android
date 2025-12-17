package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.Kakao
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

enum class ReedButtonColorStyle {
    PRIMARY, SECONDARY, TERTIARY, STROKE, TEXT, KAKAO, GOOGLE;

    @Composable
    fun containerColor(isPressed: Boolean) = when (this) {
        PRIMARY -> if (isPressed) ReedTheme.colors.bgPrimaryPressed else ReedTheme.colors.bgPrimary
        SECONDARY -> if (isPressed) ReedTheme.colors.bgSecondaryPressed else ReedTheme.colors.bgSecondary
        TERTIARY -> if (isPressed) ReedTheme.colors.bgTertiaryPressed else ReedTheme.colors.bgTertiary
        STROKE -> ReedTheme.colors.basePrimary
        TEXT -> Color.Transparent
        KAKAO -> Kakao
        GOOGLE -> ReedTheme.colors.basePrimary
    }

    @Composable
    fun contentColor() = when (this) {
        PRIMARY -> ReedTheme.colors.contentInverse
        SECONDARY -> ReedTheme.colors.contentPrimary
        TERTIARY -> ReedTheme.colors.contentBrand
        STROKE -> ReedTheme.colors.contentBrand
        TEXT -> ReedTheme.colors.contentTertiary
        KAKAO -> ReedTheme.colors.contentPrimary
        GOOGLE -> ReedTheme.colors.contentPrimary
    }

    @Composable
    fun disabledContainerColor() = when (this) {
        TEXT -> Color.Transparent
        else -> ReedTheme.colors.bgDisabled
    }

    @Composable
    fun disabledContentColor() = ReedTheme.colors.contentDisabled

    @Composable
    fun borderStroke() = when (this) {
        STROKE -> BorderStroke(1.dp, ReedTheme.colors.borderBrand)
        GOOGLE -> BorderStroke(1.dp, ReedTheme.colors.borderPrimary)
        else -> null
    }
}
