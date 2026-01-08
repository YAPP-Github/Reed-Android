package com.ninecraft.booket.feature.detail.record.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.ninecraft.booket.core.common.extensions.toFormattedDate
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.graphicRes
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.DetailEmotionModel
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.PrimaryEmotionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ReviewItem(
    primaryEmotion: PrimaryEmotionModel,
    detailEmotions: ImmutableList<DetailEmotionModel>,
    createdAt: String,
    review: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .background(color = ReedTheme.colors.baseSecondary)
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing4,
            ),
    ) {
        Column {
            if (review.isNotBlank()) {
                Text(
                    text = review,
                    color = ReedTheme.colors.contentSecondary,
                    style = ReedTheme.typography.label1Medium,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            }
            EmotionContent(primaryEmotion, detailEmotions, createdAt)
        }
    }
}

@Composable
private fun EmotionContent(
    primaryEmotion: PrimaryEmotionModel,
    detailEmotions: ImmutableList<DetailEmotionModel>,
    createdAt: String,
) {
    val hasDetailEmotion = detailEmotions.isNotEmpty()
    val primaryEmotionBackgroundColor = if (primaryEmotion.code == EmotionCode.OTHER) ReedTheme.colors.bgDisabled else ReedTheme.colors.bgTertiary
    val primaryEmotionTextColor = if (primaryEmotion.code == EmotionCode.OTHER) ReedTheme.colors.contentTertiary else ReedTheme.colors.contentBrand

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(primaryEmotion.code.graphicRes),
            contentDescription = "Emotion Graphic",
            modifier = Modifier
                .size(ReedTheme.spacing.spacing10)
                .clip(CircleShape)
                .background(ReedTheme.colors.basePrimary),
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
        Column {
            Text(
                text = primaryEmotion.displayName,
                modifier = Modifier
                    .background(
                        color = primaryEmotionBackgroundColor,
                        shape = RoundedCornerShape(ReedTheme.radius.full),
                    )
                    .padding(
                        horizontal = ReedTheme.spacing.spacing2,
                        vertical = ReedTheme.spacing.spacing1,
                    ),
                color = primaryEmotionTextColor,
                style = ReedTheme.typography.label2SemiBold,
            )

            if (hasDetailEmotion) {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
                FlowRow {
                    detailEmotions.forEach { detail ->
                        Text(
                            text = "#${detail.name}",
                            color = ReedTheme.colors.contentTertiary,
                            style = ReedTheme.typography.caption1Regular,
                        )
                        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = createdAt.toFormattedDate(),
            modifier = Modifier.align(
                if (hasDetailEmotion) Alignment.Bottom else Alignment.CenterVertically,
            ),
            color = ReedTheme.colors.contentTertiary,
            style = ReedTheme.typography.label2Regular,
        )
    }
}

@ComponentPreview
@Composable
private fun ReviewItemPreview() {
    val primaryEmotion = PrimaryEmotionModel(
        code = EmotionCode.WARMTH,
        displayName = "따뜻함",
    )

    val detailEmotions = persistentListOf(
        DetailEmotionModel(
            id = "84f95d93-e54c-11f0-8545-525ae7dd628c",
            name = "위로받은",
        ),
        DetailEmotionModel(
            id = "84f95e7e-e54c-11f0-8545-525ae7dd628c",
            name = "포근한",
        ),
        DetailEmotionModel(
            id = "84f95f13-e54c-11f0-8545-525ae7dd628c",
            name = "다정한",
        ),
        DetailEmotionModel(
            id = "84f95fc0-e54c-11f0-8545-525ae7dd628c",
            name = "고마운",
        ),
        DetailEmotionModel(
            id = "84f96094-e54c-11f0-8545-525ae7dd628c",
            name = "마음이 놓이는",
        ),
        DetailEmotionModel(
            id = "84f9612c-e54c-11f0-8545-525ae7dd628c",
            name = "편안한",
        ),
    )

    ReedTheme {
        ReviewItem(
            primaryEmotion = primaryEmotion,
            detailEmotions = detailEmotions,
            review = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다",
            createdAt = "2026-01-08T15:31:36.113488",
        )
    }
}

@ComponentPreview
@Composable
private fun EmptyReviewItemPreview() {
    val primaryEmotion = PrimaryEmotionModel(
        code = EmotionCode.WARMTH,
        displayName = "따뜻함",
    )

    val detailEmotions = persistentListOf(
        DetailEmotionModel(
            id = "84f95d93-e54c-11f0-8545-525ae7dd628c",
            name = "위로받은",
        ),
        DetailEmotionModel(
            id = "84f95e7e-e54c-11f0-8545-525ae7dd628c",
            name = "포근한",
        ),
    )

    ReedTheme {
        ReviewItem(
            primaryEmotion = primaryEmotion,
            detailEmotions = detailEmotions,
            review = "",
            createdAt = "2026-01-08T15:31:36.113488",
        )
    }
}

@ComponentPreview
@Composable
private fun EmptyDetailEmotionsReviewItemPreview() {
    val primaryEmotion = PrimaryEmotionModel(
        code = EmotionCode.WARMTH,
        displayName = "따뜻함",
    )
    ReedTheme {
        ReviewItem(
            primaryEmotion = primaryEmotion,
            detailEmotions = persistentListOf(),
            review = "",
            createdAt = "2026-01-08T15:31:36.113488",
        )
    }
}
