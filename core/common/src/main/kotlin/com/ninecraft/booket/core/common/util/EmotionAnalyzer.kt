package com.ninecraft.booket.core.common.util

import com.ninecraft.booket.core.model.EmotionModel

data class EmotionAnalysisResult(
    val topEmotions: List<EmotionModel>,
    val displayType: EmotionDisplayType,
)

enum class EmotionDisplayType {
    NONE, // 모든 감정의 count가 0
    SINGLE, // 1개 감정이 1위
    DUAL, // 2개 감정이 공동 1위
    BALANCED, // 3개 이상 감정이 공동 1위
}

fun analyzeEmotions(emotions: List<EmotionModel>): EmotionAnalysisResult {
    val maxCount = emotions.maxOf { it.count }

    // 모든 감정의 count가 0인 경우
    if (maxCount == 0) {
        return EmotionAnalysisResult(emptyList(), EmotionDisplayType.NONE)
    }

    val topEmotions = emotions.filter { it.count == maxCount }

    val displayType = when (topEmotions.size) {
        1 -> EmotionDisplayType.SINGLE
        2 -> EmotionDisplayType.DUAL
        else -> EmotionDisplayType.BALANCED
    }

    return EmotionAnalysisResult(topEmotions, displayType)
}
