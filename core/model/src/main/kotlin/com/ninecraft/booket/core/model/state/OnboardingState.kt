package com.ninecraft.booket.core.model.state

import androidx.compose.runtime.Stable

@Stable
enum class OnboardingState {
    IDLE,
    NOT_COMPLETED,
    COMPLETED,
}
