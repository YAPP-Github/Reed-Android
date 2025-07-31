package com.ninecraft.booket.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.ninecraft.booket.core.model.EmotionModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun buildEmotionText(
    emotions: ImmutableList<EmotionModel>,
    brandColor: Color,
    secondaryColor: Color,
    emotionTextStyle: TextStyle,
    regularTextStyle: TextStyle,
): AnnotatedString {
    val analysisResult = remember(emotions) { analyzeEmotions(emotions) }

    return when (analysisResult.displayType) {
        EmotionDisplayType.SINGLE -> {
            val emotion = analysisResult.topEmotions.first()
            buildAnnotatedString {
                withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                    append("이 책에서 ")
                }
                withStyle(SpanStyle(color = brandColor, fontSize = emotionTextStyle.fontSize, fontWeight = emotionTextStyle.fontWeight)) {
                    append(emotion.type.displayName)
                }
                withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                    append(" 감정을 많이 느꼈어요")
                }
            }
        }
        EmotionDisplayType.DUAL -> {
            val emotions = analysisResult.topEmotions
            buildAnnotatedString {
                withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                    append("이 책에서 ")
                }
                emotions.forEachIndexed { index, emotion ->
                    if (index > 0) {
                        withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                            append(", ")
                        }
                    }
                    withStyle(SpanStyle(color = brandColor, fontSize = emotionTextStyle.fontSize, fontWeight = emotionTextStyle.fontWeight)) {
                        append(emotion.type.displayName)
                    }
                }
                withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                    append(" 감정을 많이 느꼈어요")
                }
            }
        }
        EmotionDisplayType.BALANCED -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = secondaryColor, fontSize = regularTextStyle.fontSize, fontWeight = regularTextStyle.fontWeight)) {
                    append("이 책에서 여러 감정이 고르게 담겼어요")
                }
            }
        }
    }
}
