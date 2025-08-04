package com.ninecraft.booket.core.designsystem

import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.designsystem.theme.JoyBgColor
import com.ninecraft.booket.core.designsystem.theme.JoyTextColor
import com.ninecraft.booket.core.designsystem.theme.SadnessBgColor
import com.ninecraft.booket.core.designsystem.theme.SadnessTextColor
import com.ninecraft.booket.core.designsystem.theme.TensionBgColor
import com.ninecraft.booket.core.designsystem.theme.TensionTextColor
import com.ninecraft.booket.core.designsystem.theme.WarmthBgColor
import com.ninecraft.booket.core.designsystem.theme.WarmthTextColor

enum class EmotionTag(val label: String, val bgColor: Color, val textColor: Color, val graphic: Int) {
    WARMTH("따뜻함", WarmthBgColor, WarmthTextColor, R.drawable.img_emotion_warmth),
    JOY("즐거움", JoyBgColor, JoyTextColor, R.drawable.img_emotion_joy),
    TENSION("긴장감", TensionBgColor, TensionTextColor, R.drawable.img_emotion_tension),
    SADNESS("슬픔", SadnessBgColor, SadnessTextColor, R.drawable.img_emotion_sadness),
}
