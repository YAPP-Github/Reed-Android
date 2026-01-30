package com.ninecraft.booket.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class EmotionGroupsModel(
    val emotions: List<EmotionGroupModel>,
)

@Immutable
data class EmotionGroupModel(
    val code: EmotionCode,
    val displayName: String,
    val detailEmotions: List<DetailEmotionModel>,
)

@Immutable
data class DetailEmotionModel(
    val id: String,
    val name: String,
)

enum class EmotionCode(
    val displayName: String,
) {
    WARMTH("따뜻함"),
    JOY("즐거움"),
    SADNESS("슬픔"),
    INSIGHT("깨달음"),
    OTHER("기타"),
    ;

    companion object {
        fun fromCode(code: String): EmotionCode? {
            return EmotionCode.entries.find { it.name == code }
        }

        fun fromDisplayName(displayName: String): EmotionCode? {
            return entries.find { it.displayName == displayName }
        }
    }
}
