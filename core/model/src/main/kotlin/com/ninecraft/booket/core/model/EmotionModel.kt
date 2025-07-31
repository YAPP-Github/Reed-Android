package com.ninecraft.booket.core.model

import androidx.compose.runtime.Stable

@Stable
data class EmotionModel(
    val type: Emotion,
    val count: Int,
)

enum class Emotion(
    val displayName: String,
) {
    WARM("따뜻함"),
    JOY("즐거움"),
    TENSION("긴장감"),
    SADNESS("슬픔"),
}
