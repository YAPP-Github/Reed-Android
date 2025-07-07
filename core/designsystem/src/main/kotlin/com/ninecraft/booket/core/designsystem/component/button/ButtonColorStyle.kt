package com.ninecraft.booket.core.designsystem.component.button

import androidx.compose.runtime.Composable
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

enum class ReedButtonColorStyle {
    PRIMARY, SECONDARY, TERTIARY;

    @Composable
    fun containerColor(isPressed: Boolean) = when(this) {
        PRIMARY -> if(isPressed) ReedTheme.colors.bgPrimaryPressed else ReedTheme.colors.bgPrimary
        SECONDARY -> if(isPressed) ReedTheme.colors.bgSecondaryPressed else ReedTheme.colors.bgSecondary
        TERTIARY -> if(isPressed) ReedTheme.colors.bgTertiaryPressed else ReedTheme.colors.bgTertiary
    }

    @Composable
    fun contentColor() = when(this) {
        PRIMARY -> ReedTheme.colors.contentInverse
        SECONDARY -> ReedTheme.colors.contentPrimary
        TERTIARY -> ReedTheme.colors.contentBrand
    }

    @Composable
    fun disabledContainerColor() = ReedTheme.colors.bgDisabled

    @Composable
    fun disabledContentColor() = ReedTheme.colors.contentDisabled
}
