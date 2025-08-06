package com.ninecraft.booket.core.designsystem

import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.designsystem.theme.JoyBgColor
import com.ninecraft.booket.core.designsystem.theme.JoyTextColor
import com.ninecraft.booket.core.designsystem.theme.SadnessBgColor
import com.ninecraft.booket.core.designsystem.theme.SadnessTextColor
import com.ninecraft.booket.core.designsystem.theme.InsightBgColor
import com.ninecraft.booket.core.designsystem.theme.InsightTextColor
import com.ninecraft.booket.core.designsystem.theme.WarmthBgColor
import com.ninecraft.booket.core.designsystem.theme.WarmthTextColor

enum class EmotionTag(val label: String, val bgColor: Color, val textColor: Color, val graphic: Int) {
    WARMTH("따뜻함", WarmthBgColor, WarmthTextColor, R.drawable.img_emotion_warmth),
    JOY("즐거움", JoyBgColor, JoyTextColor, R.drawable.img_emotion_joy),
    SADNESS("슬픔", SadnessBgColor, SadnessTextColor, R.drawable.img_emotion_sadness),
    INSIGHT("깨달음", InsightBgColor, InsightTextColor, R.drawable.img_emotion_insight),
}
