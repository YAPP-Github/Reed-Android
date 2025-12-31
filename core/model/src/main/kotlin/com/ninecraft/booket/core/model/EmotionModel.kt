package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class EmotionGroupsModel(
    val emotions: List<EmotionGroupModel>,
)

@Stable
data class EmotionGroupModel(
    val code: String,
    val detailEmotions: List<DetailEmotionModel>,
)

@Stable
data class DetailEmotionModel(
    val id: String,
    val name: String,
)
