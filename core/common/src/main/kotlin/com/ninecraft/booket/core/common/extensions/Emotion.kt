package com.ninecraft.booket.core.common.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ninecraft.booket.core.model.Emotion

@Composable
fun Emotion.toTextColor(): Color {
    return when (this) {
        Emotion.WARM -> Color(0xFFE3931B)
        Emotion.JOY -> Color(0xFFEE6B33)
        Emotion.TENSION -> Color(0xFF9A55E4)
        Emotion.SADNESS -> Color(0xFF2872E9)
    }
}

@Composable
fun Emotion.toBackgroundColor(): Color {
    return when (this) {
        Emotion.WARM -> Color(0xFFFFF5D3)
        Emotion.JOY -> Color(0xFFFFEBE3)
        Emotion.TENSION -> Color(0xFFF3E8FF)
        Emotion.SADNESS -> Color(0xFFE1ECFF)
    }
}
