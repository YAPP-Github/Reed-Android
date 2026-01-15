package com.ninecraft.booket.feature.edit.emotion.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.chip.ReedSelectableChip
import com.ninecraft.booket.core.designsystem.component.chip.mediumChipStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.DetailEmotionModel
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionGroupModel
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.feature.edit.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmotionDetailBottomSheet(
    emotionGroup: EmotionGroupModel,
    selectedEmotionDetailIds: ImmutableList<String>,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onCloseButtonClick: () -> Unit,
    onEmotionDetailToggled: (String) -> Unit,
    onSkipButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.edit_emotion_detail_title, emotionGroup.displayName),
                    color = ReedTheme.colors.contentPrimary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.heading2SemiBold,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_close),
                    contentDescription = "Close Icon",
                    modifier = Modifier.clickableSingle {
                        onCloseButtonClick()
                    },
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            Text(
                text = stringResource(R.string.edit_emotion_detail_description),
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.label1Medium,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = ReedTheme.spacing.spacing5,
                        end = ReedTheme.spacing.spacing5,
                        top = ReedTheme.spacing.spacing6,
                        bottom = ReedTheme.spacing.spacing3,
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    ReedTheme.spacing.spacing2,
                    Alignment.CenterHorizontally,
                ),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                emotionGroup.detailEmotions.forEach { detail ->
                    ReedSelectableChip(
                        label = detail.name,
                        chipSizeStyle = mediumChipStyle,
                        selected = detail.id in selectedEmotionDetailIds,
                        onClick = {
                            onEmotionDetailToggled(detail.id)
                        },
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ReedTheme.spacing.spacing4),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ReedButton(
                    onClick = {
                        onSkipButtonClick()
                    },
                    text = stringResource(R.string.edit_emotion_detail_skip),
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.SECONDARY,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                ReedButton(
                    onClick = {
                        onConfirmButtonClick()
                    },
                    text = stringResource(R.string.edit_emotion_detail_confirm),
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier.weight(1f),
                    enabled = selectedEmotionDetailIds.isNotEmpty(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun EmotionDetailBottomSheetPreview() {
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
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    ReedTheme {
        EmotionDetailBottomSheet(
            emotionGroup = warmthEmotionGroup,
            selectedEmotionDetailIds = persistentListOf(),
            onDismissRequest = {},
            sheetState = sheetState,
            onCloseButtonClick = {},
            onSkipButtonClick = {},
            onConfirmButtonClick = {},
            onEmotionDetailToggled = {},
        )
    }
}
