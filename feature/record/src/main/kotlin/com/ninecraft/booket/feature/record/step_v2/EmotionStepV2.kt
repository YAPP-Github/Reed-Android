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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@TraceRecomposition
@Composable
internal fun EmotionStepV2(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    val emotionDetailBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

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
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
            }

            items(state.emotions) { emotion ->
                EmotionItem(
                    emotion = emotion,
                    selectedEmotionDetails = state.committedEmotionDetails[emotion] ?: persistentListOf(),
                    onClick = {
                        state.eventSink(RecordRegisterUiEvent.OnSelectEmotionV2(emotion))
                    },
                    isSelected = state.committedEmotion == emotion,
                    onEmotionDetailRemove = { detail ->
                        state.eventSink(RecordRegisterUiEvent.OnEmotionDetailRemoved(detail))
                    },
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
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
            text = stringResource(R.string.record_finish_button_text),
            multipleEventsCutterEnabled = false,
        )
    }

    if (state.isEmotionDetailBottomSheetVisible) {
        EmotionDetailBottomSheet(
            emotion = state.selectedEmotion ?: Emotion.WARM,
            emotionDetails = state.emotionDetails,
            selectedEmotionDetail = state.selectedEmotionDetails[state.selectedEmotion] ?: persistentListOf(),
            onDismissRequest = {
                state.eventSink(RecordRegisterUiEvent.OnEmotionDetailBottomSheetDismiss)
            },
            sheetState = emotionDetailBottomSheetState,
            onCloseButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(RecordRegisterUiEvent.OnEmotionDetailBottomSheetDismiss)
                }
            },
            onEmotionDetailToggled = { detail ->
                state.eventSink(RecordRegisterUiEvent.OnEmotionDetailToggled(detail))
            },
            onSkipButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(RecordRegisterUiEvent.OnEmotionDetailSkipped)
                }
            },
            onConfirmButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(RecordRegisterUiEvent.OnEmotionDetailCommitted)
                }
            },
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
