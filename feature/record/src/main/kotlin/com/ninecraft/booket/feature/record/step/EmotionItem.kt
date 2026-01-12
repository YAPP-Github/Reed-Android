package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.component.chip.ReedRemovableChip
import com.ninecraft.booket.core.designsystem.component.chip.smallChipStyle
import com.ninecraft.booket.core.designsystem.descriptionRes
import com.ninecraft.booket.core.designsystem.categoryGraphicRes
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.DetailEmotionModel
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionGroupModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun EmotionItem(
    emotionGroup: EmotionGroupModel,
    selectedEmotionDetailIds: ImmutableList<String>,
    onClick: () -> Unit,
    isSelected: Boolean,
    onEmotionDetailRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerShape = RoundedCornerShape(ReedTheme.radius.md)
    val iconRes = if (isSelected) R.drawable.ic_check else R.drawable.ic_chevron_right
    val iconTint = if (isSelected) ReedTheme.colors.borderBrand else ReedTheme.colors.contentTertiary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cornerShape)
            .clickable {
                onClick()
            }
            .background(color = ReedTheme.colors.baseSecondary)
            .then(
                if (isSelected) Modifier.border(
                    width = ReedTheme.border.border15,
                    color = ReedTheme.colors.borderBrand,
                    shape = cornerShape,
                )
                else Modifier,
            )
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing3,
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val emotionGraphicRes = emotionGroup.code.categoryGraphicRes
            if (emotionGraphicRes != null) {
                Image(
                    painter = painterResource(emotionGraphicRes),
                    contentDescription = "Emotion Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
            }

            Column {
                Text(
                    text = emotionGroup.displayName,
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.headline1SemiBold,
                )
                Text(
                    text = stringResource(emotionGroup.code.descriptionRes),
                    color = ReedTheme.colors.contentTertiary,
                    style = ReedTheme.typography.label1Medium,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = "Chevron Right",
                tint = iconTint,
            )
        }

        if (selectedEmotionDetailIds.isNotEmpty()) {
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                selectedEmotionDetailIds.forEach { detailId ->
                    val detailName = emotionGroup.detailEmotions.firstOrNull { it.id == detailId }?.name ?: return@forEach
                    ReedRemovableChip(
                        label = detailName,
                        chipSizeStyle = smallChipStyle,
                        onRemove = {
                            onEmotionDetailRemove(detailId)
                        },
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun EmotionItemPreview() {
    val warmthEmotionGroup = EmotionGroupModel(
        code = EmotionCode.WARMTH,
        displayName = "따뜻함",
        detailEmotions = persistentListOf(
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
        ),
    )

    val selectedEmotionDetailIds = persistentListOf(
        "84f95fc0-e54c-11f0-8545-525ae7dd628c",
        "84f96094-e54c-11f0-8545-525ae7dd628c",
    )

    ReedTheme {
        EmotionItem(
            emotionGroup = warmthEmotionGroup,
            selectedEmotionDetailIds = selectedEmotionDetailIds,
            onClick = {},
            isSelected = false,
            onEmotionDetailRemove = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
