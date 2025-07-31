package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.theme.Kakao
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

enum class ReedButtonColorStyle {
    PRIMARY, PRIMARY_INVERSE_TEXT, SECONDARY, TERTIARY, STROKE, KAKAO;

    @Composable
    fun containerColor(isPressed: Boolean) = when (this) {
        PRIMARY -> if (isPressed) ReedTheme.colors.bgPrimaryPressed else ReedTheme.colors.bgPrimary
        PRIMARY_INVERSE_TEXT -> if (isPressed) ReedTheme.colors.bgPrimaryPressed else ReedTheme.colors.bgPrimary
        SECONDARY -> if (isPressed) ReedTheme.colors.bgSecondaryPressed else ReedTheme.colors.bgSecondary
        TERTIARY -> if (isPressed) ReedTheme.colors.bgTertiaryPressed else ReedTheme.colors.bgTertiary
        STROKE -> if (isPressed) ReedTheme.colors.basePrimary else ReedTheme.colors.basePrimary
        KAKAO -> Kakao
    }

    @Composable
    fun contentColor() = when (this) {
        PRIMARY -> ReedTheme.colors.contentInverse
        PRIMARY_INVERSE_TEXT -> ReedTheme.colors.contentInverse
        SECONDARY -> ReedTheme.colors.contentPrimary
        TERTIARY -> ReedTheme.colors.contentBrand
        STROKE -> ReedTheme.colors.contentBrand
        KAKAO -> ReedTheme.colors.contentPrimary
    }

    @Composable
    fun disabledContainerColor() = ReedTheme.colors.bgDisabled

    @Composable
    fun disabledContentColor() = if (this == PRIMARY_INVERSE_TEXT) ReedTheme.colors.contentInverse else ReedTheme.colors.contentDisabled

    @Composable
    fun borderStroke() = when (this) {
        STROKE -> BorderStroke(1.dp, ReedTheme.colors.borderBrand)
        else -> null
    }
}
