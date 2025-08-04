package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.util.analyzeEmotions
import com.ninecraft.booket.core.common.util.EmotionDisplayType
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun EmotionAnalysisResultText(
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
                    append("이 책에서 ")
                }
                withStyle(SpanStyle(color = brandColor, fontSize = emotionTextStyle.fontSize, fontWeight = emotionTextStyle.fontWeight)) {
                    append("여러 감정이 고르게 담겼어요")
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun EmotionTextAllCasesPreview() {
    ReedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "1개의 감정이 1위인 경우:")
            Text(
                text = EmotionAnalysisResultText(
                    emotions = persistentListOf(
                        EmotionModel(type = Emotion.WARM, count = 5),
                        EmotionModel(type = Emotion.JOY, count = 2),
                    ),
                    brandColor = ReedTheme.colors.contentBrand,
                    secondaryColor = ReedTheme.colors.contentSecondary,
                    emotionTextStyle = ReedTheme.typography.label2SemiBold,
                    regularTextStyle = ReedTheme.typography.label2Regular,
                ),
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "2개의 감정이 공동 1위인 경우:")
            Text(
                text = EmotionAnalysisResultText(
                    emotions = persistentListOf(
                        EmotionModel(type = Emotion.WARM, count = 5),
                        EmotionModel(type = Emotion.JOY, count = 5),
                        EmotionModel(type = Emotion.SADNESS, count = 2),
                    ),
                    brandColor = ReedTheme.colors.contentBrand,
                    secondaryColor = ReedTheme.colors.contentSecondary,
                    emotionTextStyle = ReedTheme.typography.label2SemiBold,
                    regularTextStyle = ReedTheme.typography.label2Regular,
                ),
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "3~4개의 감정이 공동 1위인 경우:")
            Text(
                text = EmotionAnalysisResultText(
                    emotions = persistentListOf(
                        EmotionModel(type = Emotion.WARM, count = 3),
                        EmotionModel(type = Emotion.JOY, count = 3),
                        EmotionModel(type = Emotion.SADNESS, count = 3),
                        EmotionModel(type = Emotion.TENSION, count = 3),
                    ),
                    brandColor = ReedTheme.colors.contentBrand,
                    secondaryColor = ReedTheme.colors.contentSecondary,
                    emotionTextStyle = ReedTheme.typography.label2SemiBold,
                    regularTextStyle = ReedTheme.typography.label2Regular,
                ),
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}
