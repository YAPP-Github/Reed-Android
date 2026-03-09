package com.ninecraft.booket.feature.edit.record.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.graphicRes
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.feature.screens.arguments.DetailEmotionArg
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun EmotionItem(
    primaryEmotionCode: EmotionCode,
    primaryEmotionName: String,
    detailEmotions: ImmutableList<DetailEmotionArg>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .background(color = ReedTheme.colors.baseSecondary)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing4,
            ),
    ) {
        EmotionContent(primaryEmotionCode, primaryEmotionName, detailEmotions)
    }
}

@Composable
private fun EmotionContent(
    primaryEmotionCode: EmotionCode,
    primaryEmotionName: String,
    detailEmotions: ImmutableList<DetailEmotionArg>,
) {
    val hasDetailEmotion = detailEmotions.isNotEmpty()
    val primaryEmotionBackgroundColor = if (primaryEmotionCode == EmotionCode.OTHER) ReedTheme.colors.bgDisabled else ReedTheme.colors.bgTertiary
    val primaryEmotionTextColor = if (primaryEmotionCode == EmotionCode.OTHER) ReedTheme.colors.contentTertiary else ReedTheme.colors.contentBrand

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(primaryEmotionCode.graphicRes),
            contentDescription = "Emotion Graphic",
            modifier = Modifier
                .size(ReedTheme.spacing.spacing10)
                .clip(CircleShape)
                .background(ReedTheme.colors.basePrimary),
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
        Column {
            Text(
                text = primaryEmotionName,
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
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
            contentDescription = "Chevron Right Icon",
            tint = ReedTheme.colors.contentSecondary,
        )
    }
}

@ComponentPreview
@Composable
private fun EmotionItemPreview() {
    val primaryEmotionName = "따뜻함"

    val detailEmotions = persistentListOf(
        DetailEmotionArg(
            id = "84f95d93-e54c-11f0-8545-525ae7dd628c",
            name = "위로받은",
        ),
        DetailEmotionArg(
            id = "84f95e7e-e54c-11f0-8545-525ae7dd628c",
            name = "포근한",
        ),
    )

    ReedTheme {
        EmotionItem(
            primaryEmotionName = primaryEmotionName,
            primaryEmotionCode = EmotionCode.WARMTH,
            detailEmotions = detailEmotions,
            onClick = {},
        )
    }
}
