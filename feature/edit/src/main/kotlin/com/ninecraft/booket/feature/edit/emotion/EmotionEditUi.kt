package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.feature.edit.R
import com.ninecraft.booket.feature.edit.emotion.component.EmotionDetailBottomSheet
import com.ninecraft.booket.feature.edit.emotion.component.EmotionItem
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@TraceRecomposition
@CircuitInject(EmotionEditScreen::class, AppScope::class)
@Composable
internal fun EmotionEditUi(
    state: EmotionEditUiState,
    modifier: Modifier = Modifier,
) {
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedBackTopAppBar(
                onBackClick = {
                    state.eventSink(EmotionEditUiEvent.OnBackClick)
                },
            )
            EmotionEditContent(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@TraceRecomposition
@Composable
private fun EmotionEditContent(
    state: EmotionEditUiState,
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
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            }
            item {
                Text(
                    text = stringResource(R.string.edit_emotion_title),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.heading1Bold,
                )
            }
            item {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            }
            item {
                Text(
                    text = stringResource(R.string.edit_emotion_description),
                    color = ReedTheme.colors.contentTertiary,
                    style = ReedTheme.typography.label1Medium,
                )
            }
            item {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
            }

            items(state.emotionGroups) { emotion ->
                EmotionItem(
                    emotionGroup = emotion,
                    selectedEmotionDetailIds = state.committedEmotionMap[emotion.code] ?: persistentListOf(),
                    onClick = {
                        state.eventSink(EmotionEditUiEvent.OnSelectEmotionCode(emotion.code))
                    },
                    isSelected = state.committedEmotion == emotion.code,
                    onEmotionDetailRemove = { detail ->
                        state.eventSink(EmotionEditUiEvent.OnEmotionDetailRemoved(detail))
                    },
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            }
        }

        ReedButton(
            onClick = {
                 state.eventSink(EmotionEditUiEvent.OnEditButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = ReedTheme.spacing.spacing5)
                .padding(bottom = ReedTheme.spacing.spacing4),
            enabled = state.isEditButtonEnabled,
            text = stringResource(R.string.edit_emotion_edit),
        )
    }

    if (state.isEmotionDetailBottomSheetVisible) {
        val selectedEmotionGroup = state.emotionGroups.firstOrNull { it.code == state.selectedEmotionCode } ?: return
        EmotionDetailBottomSheet(
            emotionGroup = selectedEmotionGroup,
            selectedEmotionDetailIds = state.selectedEmotionMap[state.selectedEmotionCode] ?: persistentListOf(),
            onDismissRequest = {
                state.eventSink(EmotionEditUiEvent.OnEmotionDetailBottomSheetDismiss)
            },
            sheetState = emotionDetailBottomSheetState,
            onCloseButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(EmotionEditUiEvent.OnEmotionDetailBottomSheetDismiss)
                }
            },
            onEmotionDetailToggled = { detail ->
                state.eventSink(EmotionEditUiEvent.OnEmotionDetailToggled(detail))
            },
            onSkipButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(EmotionEditUiEvent.OnEmotionDetailSkipped)
                }
            },
            onConfirmButtonClick = {
                coroutineScope.launch {
                    emotionDetailBottomSheetState.hide()
                    state.eventSink(EmotionEditUiEvent.OnEmotionDetailCommitted)
                }
            },
        )
    }
}

@ComponentPreview
@Composable
private fun EmotionEditUiPreview() {
    ReedTheme {
        EmotionEditUi(
            state = EmotionEditUiState(
                eventSink = {},
            ),
        )
    }
}
