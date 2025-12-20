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

val Emotion.graphicResV2: Int
    get() = when (this) {
        Emotion.WARM -> R.drawable.img_warm
        Emotion.JOY -> R.drawable.img_joy
        Emotion.SAD -> R.drawable.img_sad
        Emotion.INSIGHT -> R.drawable.img_insight
    }

val Emotion.descriptionRes: Int
    get() = when (this) {
        Emotion.WARM -> R.string.emotion_warm_description
        Emotion.JOY -> R.string.emotion_joy_description
        Emotion.SAD -> R.string.emotion_sad_description
        Emotion.INSIGHT -> R.string.emotion_insight_description
    }
