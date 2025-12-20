package com.ninecraft.booket.feature.record.step_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.register.RecordRegisterUiEvent
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState
import com.skydoves.compose.stability.runtime.TraceRecomposition
import kotlinx.collections.immutable.toPersistentList

@TraceRecomposition
@Composable
internal fun EmotionStepV2(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = White),
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

            items(state.emotions) { emotion ->
                EmotionItem(
                    emotion = emotion,
                    onClick = {
                        state.eventSink(RecordRegisterUiEvent.OnSelectEmotion(emotion))
                    },
                    isSelected = state.selectedEmotion == emotion,
                )
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
            text = stringResource(R.string.record_next_button_text),
            multipleEventsCutterEnabled = false,
        )
    }
}

@ComponentPreview
@Composable
private fun EmotionStepV2Preview() {
    val emotions = Emotion.entries.toPersistentList()

    ReedTheme {
        EmotionStepV2(
            state = RecordRegisterUiState(
                emotions = emotions,
                eventSink = {},
            ),
        )
    }
}
