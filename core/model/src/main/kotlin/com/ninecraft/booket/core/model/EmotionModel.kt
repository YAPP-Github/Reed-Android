package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class EmotionGroupsModel(
    val emotions: List<EmotionGroupModel>,
)

@Stable
data class EmotionGroupModel(
    val code: EmotionCode,
    val displayName: String,
    val detailEmotions: List<DetailEmotionModel>,
)

@Stable
data class DetailEmotionModel(
    val id: String,
    val name: String,
)

enum class EmotionCode {
    WARMTH, JOY, SADNESS, INSIGHT, OTHER;

    companion object {
        fun fromCode(code: String): EmotionCode? {
            return EmotionCode.entries.find { it.name == code }
        }
    }
}
