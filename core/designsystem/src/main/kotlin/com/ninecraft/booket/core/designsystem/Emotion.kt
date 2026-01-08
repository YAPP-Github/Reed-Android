package com.ninecraft.booket.core.designsystem

import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.designsystem.theme.InsightBgColor
import com.ninecraft.booket.core.designsystem.theme.InsightTextColor
import com.ninecraft.booket.core.designsystem.theme.JoyBgColor
import com.ninecraft.booket.core.designsystem.theme.JoyTextColor
import com.ninecraft.booket.core.designsystem.theme.SadnessBgColor
import com.ninecraft.booket.core.designsystem.theme.SadnessTextColor
import com.ninecraft.booket.core.designsystem.theme.WarmthBgColor
import com.ninecraft.booket.core.designsystem.theme.WarmthTextColor
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionCode

val Emotion.bgColor: Color
    get() = when (this) {
        Emotion.WARM -> WarmthBgColor
        Emotion.JOY -> JoyBgColor
        Emotion.SAD -> SadnessBgColor
        Emotion.INSIGHT -> InsightBgColor
    }

val Emotion.textColor: Color
    get() = when (this) {
        Emotion.WARM -> WarmthTextColor
        Emotion.JOY -> JoyTextColor
        Emotion.SAD -> SadnessTextColor
        Emotion.INSIGHT -> InsightTextColor
    }

val Emotion.graphicRes: Int
    get() = when (this) {
        Emotion.WARM -> R.drawable.img_emotion_warmth
        Emotion.JOY -> R.drawable.img_emotion_joy
        Emotion.SAD -> R.drawable.img_emotion_sadness
        Emotion.INSIGHT -> R.drawable.img_emotion_insight
    }

val EmotionCode.graphicResV2: Int?
    get() = when (this) {
        EmotionCode.WARMTH -> R.drawable.img_category_warm
        EmotionCode.JOY -> R.drawable.img_category_joy
        EmotionCode.SADNESS -> R.drawable.img_category_sad
        EmotionCode.INSIGHT -> R.drawable.img_category_insight
        EmotionCode.OTHER -> null
    }

val EmotionCode.descriptionRes: Int
    get() = when (this) {
        EmotionCode.WARMTH -> R.string.emotion_warm_description
        EmotionCode.JOY -> R.string.emotion_joy_description
        EmotionCode.SADNESS -> R.string.emotion_sad_description
        EmotionCode.INSIGHT -> R.string.emotion_insight_description
        EmotionCode.OTHER -> R.string.emotion_other_description
    }
