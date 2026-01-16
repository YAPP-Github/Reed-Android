package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class SeedModel(
    val categories: List<EmotionModel> = emptyList(),
)

@Stable
data class EmotionModel(
    val code: EmotionCode,
    val count: Int,
)
