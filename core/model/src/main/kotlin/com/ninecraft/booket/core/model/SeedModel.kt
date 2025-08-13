package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class SeedModel(
    val categories: List<EmotionModel> = emptyList(),
)

@Stable
data class EmotionModel(
    val name: Emotion,
    val count: Int,
)

enum class Emotion(
    val displayName: String,
) {
    WARM("따뜻함"),
    JOY("즐거움"),
    SAD("슬픔"),
    INSIGHT("깨달음"),
    ;

    companion object {
        fun fromDisplayName(displayName: String): Emotion? {
            return entries.find { it.displayName == displayName }
        }
    }
}
