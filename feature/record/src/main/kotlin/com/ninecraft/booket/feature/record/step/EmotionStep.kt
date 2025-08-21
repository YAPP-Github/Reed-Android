package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.register.RecordRegisterUiEvent
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState
import kotlinx.collections.immutable.toPersistentList

@Composable
fun EmotionStep(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    val emotionPairs = remember(state.emotionTags) { state.emotionTags.chunked(2) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ReedTheme.spacing.spacing5)
                .padding(bottom = 80.dp),
        ) {
            item {
                Text(
                    text = stringResource(R.string.emotion_step_title),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.heading1Bold,
                )
            }
            item {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            }
            item {
                Text(
                    text = stringResource(R.string.emotion_step_description),
                    color = ReedTheme.colors.contentTertiary,
                    style = ReedTheme.typography.label1Medium,
                )
            }
            item {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            }

            items(emotionPairs) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
                ) {
                    pair.forEach { tag ->
                        EmotionItem(
                            emotionTag = tag,
                            onClick = {
                                state.eventSink(RecordRegisterUiEvent.OnSelectEmotion(tag))
                            },
                            isSelected = state.selectedEmotion == tag,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            }
        }

        ReedButton(
            onClick = {
                state.eventSink(RecordRegisterUiEvent.OnNextButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = ReedTheme.spacing.spacing5)
                .padding(bottom = ReedTheme.spacing.spacing4),
            enabled = state.isNextButtonEnabled,
            text = stringResource(R.string.record_next_button),
            multipleEventsCutterEnabled = state.currentStep == RecordStep.IMPRESSION,
        )
    }
}

@Composable
private fun EmotionItem(
    emotionTag: EmotionTag,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(214.dp)
            .background(
                color = ReedTheme.colors.bgTertiary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            )
            .then(
                if (isSelected) Modifier.border(
                    width = ReedTheme.border.border15,
                    color = ReedTheme.colors.borderBrand,
                    shape = RoundedCornerShape(ReedTheme.radius.md),
                )
                else Modifier,
            )
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .clickableSingle {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(emotionTag.graphic),
            contentDescription = "Emotion Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@ComponentPreview
@Composable
private fun RecordRegisterPreview() {
    val emotionTags = EmotionTag.entries.toPersistentList()

    ReedTheme {
        EmotionStep(
            state = RecordRegisterUiState(
                emotionTags = emotionTags,
                eventSink = {},
            ),
        )
    }
}
