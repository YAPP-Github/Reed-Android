package com.ninecraft.booket.feature.record.step_v2

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
import com.ninecraft.booket.core.designsystem.graphicResV2
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.Emotion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun EmotionItem(
    emotion: Emotion,
    selectedEmotionDetails: ImmutableList<String>,
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
            Image(
                painter = painterResource(emotion.graphicResV2),
                contentDescription = "Emotion Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
            Column {
                Text(
                    text = emotion.displayName,
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.headline1SemiBold,
                )
                Text(
                    text = stringResource(emotion.descriptionRes),
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

        if (selectedEmotionDetails.isNotEmpty()) {
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                selectedEmotionDetails.forEach { detail ->
                    ReedRemovableChip(
                        label = detail,
                        chipSizeStyle = smallChipStyle,
                        onRemove = {
                            onEmotionDetailRemove(detail)
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
    val selectedEmotionDetails = persistentListOf("위로받은", "포근한", "다정한", "고마운", "마음이 놓이는", "편안한")

    ReedTheme {
        EmotionItem(
            emotion = Emotion.WARM,
            selectedEmotionDetails = selectedEmotionDetails,
            onClick = {},
            isSelected = false,
            onEmotionDetailRemove = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
