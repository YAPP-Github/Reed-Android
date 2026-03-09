package com.ninecraft.booket.core.designsystem

import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.designsystem.theme.Blue300
import com.ninecraft.booket.core.designsystem.theme.Blue500
import com.ninecraft.booket.core.designsystem.theme.InsightBgColor
import com.ninecraft.booket.core.designsystem.theme.InsightTextColor
import com.ninecraft.booket.core.designsystem.theme.JoyBgColor
import com.ninecraft.booket.core.designsystem.theme.JoyTextColor
import com.ninecraft.booket.core.designsystem.theme.Neutral300
import com.ninecraft.booket.core.designsystem.theme.Neutral500
import com.ninecraft.booket.core.designsystem.theme.Orange300
import com.ninecraft.booket.core.designsystem.theme.Orange400
import com.ninecraft.booket.core.designsystem.theme.OtherBgColor
import com.ninecraft.booket.core.designsystem.theme.OtherTextColor
import com.ninecraft.booket.core.designsystem.theme.SadnessBgColor
import com.ninecraft.booket.core.designsystem.theme.SadnessTextColor
import com.ninecraft.booket.core.designsystem.theme.Violet300
import com.ninecraft.booket.core.designsystem.theme.Violet500
import com.ninecraft.booket.core.designsystem.theme.WarmthBgColor
import com.ninecraft.booket.core.designsystem.theme.WarmthTextColor
import com.ninecraft.booket.core.designsystem.theme.Yellow300
import com.ninecraft.booket.core.designsystem.theme.Yellow700
import com.ninecraft.booket.core.model.EmotionCode

val EmotionCode.bgColor: Color
    get() = when (this) {
        EmotionCode.WARMTH -> WarmthBgColor
        EmotionCode.JOY -> JoyBgColor
        EmotionCode.SADNESS -> SadnessBgColor
        EmotionCode.INSIGHT -> InsightBgColor
        EmotionCode.OTHER -> OtherBgColor
    }

val EmotionCode.textColor: Color
    get() = when (this) {
        EmotionCode.WARMTH -> WarmthTextColor
        EmotionCode.JOY -> JoyTextColor
        EmotionCode.SADNESS -> SadnessTextColor
        EmotionCode.INSIGHT -> InsightTextColor
        EmotionCode.OTHER -> OtherTextColor
    }

val EmotionCode.primaryEmotionColor: Color
    get() = when (this) {
        EmotionCode.WARMTH -> Yellow700
        EmotionCode.JOY -> Orange400
        EmotionCode.SADNESS -> Blue500
        EmotionCode.INSIGHT -> Violet500
        EmotionCode.OTHER -> Neutral500
    }

val EmotionCode.ratioBarColor: Color
    get() = when (this) {
        EmotionCode.WARMTH -> Yellow300
        EmotionCode.JOY -> Orange300
        EmotionCode.SADNESS -> Blue300
        EmotionCode.INSIGHT -> Violet300
        EmotionCode.OTHER -> Neutral300
    }

val EmotionCode.graphicRes: Int
    get() = when (this) {
        EmotionCode.WARMTH -> R.drawable.img_warmth
        EmotionCode.JOY -> R.drawable.img_joy
        EmotionCode.SADNESS -> R.drawable.img_sadness
        EmotionCode.INSIGHT -> R.drawable.img_insight
        EmotionCode.OTHER -> R.drawable.img_other
    }

val EmotionCode.categoryGraphicRes: Int?
    get() = when (this) {
        EmotionCode.WARMTH -> R.drawable.img_category_warmth
        EmotionCode.JOY -> R.drawable.img_category_joy
        EmotionCode.SADNESS -> R.drawable.img_category_sadness
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
