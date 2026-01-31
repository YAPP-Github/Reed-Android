package com.ninecraft.booket.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class SeedModel(
    val categories: List<EmotionModel> = emptyList(),
)

@Immutable
data class EmotionModel(
    val code: EmotionCode,
    val count: Int,
)
