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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.graphicRes
import com.ninecraft.booket.core.designsystem.primaryEmotionColor
import com.ninecraft.booket.core.designsystem.ratioBarColor
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.PrimaryEmotionModel
import com.ninecraft.booket.feature.detail.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun CollectedSeeds(
    seedsStats: ImmutableList<EmotionModel>,
    representativeEmotion: PrimaryEmotionModel,
    isStatsExpanded: Boolean,
    onToggleClick: () -> Unit,
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
            .background(ReedTheme.colors.baseSecondary)
            .padding(ReedTheme.spacing.spacing4),
    ) {
        CollectedSeedsHeader(
            primaryEmotion = representativeEmotion,
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

                val displayEmotions = listOf(
                    EmotionCode.WARMTH,
                    EmotionCode.JOY,
                    EmotionCode.SADNESS,
                    EmotionCode.INSIGHT,
                    EmotionCode.OTHER,
                ).mapNotNull { emotionCode ->
                    seedsStats.find { it.code == emotionCode && it.count > 0 }
                }

                if (displayEmotions.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing1),
                    ) {
                        displayEmotions.forEach { emotionModel ->
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
}

@Composable
private fun CollectedSeedsHeader(
    primaryEmotion: PrimaryEmotionModel,
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
            primaryEmotion.let { emotion ->
                Image(
                    painter = painterResource(id = emotion.code.graphicRes),
                    contentDescription = "Seed Image",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ReedTheme.colors.basePrimary),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "'${primaryEmotion.code.displayName}'",
                    color = primaryEmotion.code.primaryEmotionColor,
                    style = ReedTheme.typography.label1SemiBold,
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
                Text(
                    text = stringResource(
                        if (primaryEmotion.code == EmotionCode.OTHER) {
                            R.string.collected_seed_other_emotion_description
                        } else {
                            R.string.collected_seed_emotion_description
                        },
                    ),
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
        EmotionCode.entries.forEach { emotionCode ->
            val emotionModel = seedsStats.find { it.code == emotionCode }
            val count = emotionModel?.count ?: 0
            if (count > 0) {
                val weight = count.toFloat() / totalCount
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .height(12.dp)
                        .background(emotionCode.ratioBarColor),
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
                .background(emotion.code.ratioBarColor),
        )

        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))

        Text(
            text = emotion.code.displayName,
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
                EmotionModel(EmotionCode.WARMTH, 4),
                EmotionModel(EmotionCode.JOY, 2),
                EmotionModel(EmotionCode.SADNESS, 2),
                EmotionModel(EmotionCode.INSIGHT, 2),
                EmotionModel(EmotionCode.OTHER, 2),
            ),
            representativeEmotion = PrimaryEmotionModel(EmotionCode.WARMTH, "따뜻함"),
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
                EmotionModel(EmotionCode.WARMTH, 2),
                EmotionModel(EmotionCode.JOY, 4),
                EmotionModel(EmotionCode.SADNESS, 2),
                EmotionModel(EmotionCode.INSIGHT, 2),
                EmotionModel(EmotionCode.OTHER, 2),
            ),
            representativeEmotion = PrimaryEmotionModel(EmotionCode.JOY, "즐거움"),
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
                EmotionModel(EmotionCode.WARMTH, 4),
                EmotionModel(EmotionCode.JOY, 4),
                EmotionModel(EmotionCode.SADNESS, 2),
                EmotionModel(EmotionCode.INSIGHT, 2),
                EmotionModel(EmotionCode.OTHER, 2),
            ),
            representativeEmotion = PrimaryEmotionModel(EmotionCode.WARMTH, "따뜻함"),
            isStatsExpanded = true,
            onToggleClick = {},
        )
    }
}
