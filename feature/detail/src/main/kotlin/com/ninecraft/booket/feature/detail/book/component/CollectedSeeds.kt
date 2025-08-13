package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.feature.detail.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun CollectedSeeds(
    seedsStats: ImmutableList<EmotionModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing5,
                end = ReedTheme.spacing.spacing5,
                bottom = ReedTheme.spacing.spacing6,
            )
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .background(ReedTheme.colors.baseSecondary),
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        Text(
            text = stringResource(R.string.collected_seed_title),
            modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing4),
            color = ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.body2Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            seedsStats.forEach { emotion ->
                SeedItem(emotion = emotion)
            }
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReedTheme.spacing.spacing4)
                .clip(RoundedCornerShape(ReedTheme.radius.sm))
                .background(ReedTheme.colors.basePrimary)
                .border(
                    width = 1.dp,
                    color = ReedTheme.colors.borderPrimary,
                    shape = RoundedCornerShape(ReedTheme.radius.sm),
                )
                .padding(ReedTheme.spacing.spacing3),
        ) {
            EmotionAnalysisResultText(
                emotions = seedsStats,
                brandColor = ReedTheme.colors.contentBrand,
                secondaryColor = ReedTheme.colors.contentSecondary,
                emotionTextStyle = ReedTheme.typography.label2SemiBold,
                regularTextStyle = ReedTheme.typography.label2Regular,
            )?.let { annotatedString ->
                Text(
                    text = annotatedString,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
    }
}

@ComponentPreview
@Composable
private fun CollectedSeedPreview() {
    ReedTheme {
        CollectedSeeds(
            seedsStats = persistentListOf(
                EmotionModel(Emotion.WARM, 3),
                EmotionModel(Emotion.JOY, 2),
                EmotionModel(Emotion.SAD, 1),
                EmotionModel(Emotion.INSIGHT, 3),
            ),
        )
    }
}
