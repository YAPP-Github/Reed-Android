package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.analyzeEmotions
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.ratioBarColor
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.Yellow700
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun CollectedSeeds(
    seedsStats: ImmutableList<EmotionModel>,
    isStatsExpanded: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val analysisResult = remember(seedsStats) { analyzeEmotions(seedsStats) }
    val topEmotion = analysisResult.topEmotions.firstOrNull()

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
            .background(ReedTheme.colors.baseSecondary)
            .padding(ReedTheme.spacing.spacing4),
    ) {
        CollectedSeedsHeader(
            topEmotion = topEmotion,
            isStatsExpanded = isStatsExpanded,
            onToggleClick = onToggleClick,
        )

        AnimatedVisibility(
            visible = isStatsExpanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
                HorizontalDivider(
                    color = ReedTheme.colors.dividerSm,
                    thickness = 1.dp,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))

                EmotionRatioBar(seedsStats = seedsStats)

                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing1),
                ) {
                    Emotion.entries.forEach { emotion ->
                        val emotionModel = seedsStats.find { it.name == emotion }
                            ?: EmotionModel(emotion, 0)
                        EmotionStatCard(
                            emotion = emotionModel,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectedSeedsHeader(
    topEmotion: EmotionModel?,
    isStatsExpanded: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            topEmotion?.let { emotion ->
                Image(
                    painter = painterResource(id = getEmotionImageResourceByDisplayName(emotion.name.displayName)),
                    contentDescription = "Seed Image",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ReedTheme.colors.basePrimary),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            }

            Row {
                Text(
                    text = "'${topEmotion?.name?.displayName ?: ""}'",
                    color = Yellow700,
                    style = ReedTheme.typography.label1SemiBold,
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
                Text(
                    text = "감정을 많이 느꼈어요",
                    color = ReedTheme.colors.contentSecondary,
                    style = ReedTheme.typography.label1Medium,
                )
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(
                if (isStatsExpanded) designR.drawable.ic_chevron_up else designR.drawable.ic_chevron_down,
            ),
            contentDescription = if (isStatsExpanded) "Collapse" else "Expand",
            modifier = Modifier.size(24.dp),
            tint = ReedTheme.colors.contentTertiary,
        )
    }
}

@Composable
private fun EmotionRatioBar(
    seedsStats: ImmutableList<EmotionModel>,
    modifier: Modifier = Modifier,
) {
    val totalCount = seedsStats.sumOf { it.count }.coerceAtLeast(1)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(ReedTheme.radius.full)),
    ) {
        Emotion.entries.forEach { emotion ->
            val emotionModel = seedsStats.find { it.name == emotion }
            val count = emotionModel?.count ?: 0
            if (count > 0) {
                val weight = count.toFloat() / totalCount
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .height(12.dp)
                        .background(emotion.ratioBarColor),
                )
            }
        }
    }
}

@Composable
private fun EmotionStatCard(
    emotion: EmotionModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .background(ReedTheme.colors.basePrimary)
            .padding(
                top = ReedTheme.spacing.spacing3,
                bottom = ReedTheme.spacing.spacing2,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(ReedTheme.radius.xs))
                .background(emotion.name.ratioBarColor),
        )

        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))

        Text(
            text = emotion.name.displayName,
            color = ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.label2Regular,
        )

        Text(
            text = "${emotion.count}개",
            color = ReedTheme.colors.contentTertiary,
            style = ReedTheme.typography.caption1Regular,
        )
    }
}

@ComponentPreview
@Composable
private fun CollectedSeedsCollapsedPreview() {
    ReedTheme {
        CollectedSeeds(
            seedsStats = persistentListOf(
                EmotionModel(Emotion.WARM, 4),
                EmotionModel(Emotion.JOY, 2),
                EmotionModel(Emotion.SAD, 2),
                EmotionModel(Emotion.INSIGHT, 2),
                EmotionModel(Emotion.ETC, 2),
            ),
            isStatsExpanded = false,
            onToggleClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun CollectedSeedsExpandedPreview() {
    ReedTheme {
        CollectedSeeds(
            seedsStats = persistentListOf(
                EmotionModel(Emotion.WARM, 4),
                EmotionModel(Emotion.JOY, 2),
                EmotionModel(Emotion.SAD, 2),
                EmotionModel(Emotion.INSIGHT, 2),
                EmotionModel(Emotion.ETC, 2),
            ),
            isStatsExpanded = true,
            onToggleClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun CollectedSeedsExpandedDuplicatedPreview() {
    ReedTheme {
        CollectedSeeds(
            seedsStats = persistentListOf(
                EmotionModel(Emotion.WARM, 4),
                EmotionModel(Emotion.JOY, 4),
                EmotionModel(Emotion.SAD, 2),
                EmotionModel(Emotion.INSIGHT, 2),
                EmotionModel(Emotion.ETC, 2),
            ),
            isStatsExpanded = true,
            onToggleClick = {},
        )
    }
}
