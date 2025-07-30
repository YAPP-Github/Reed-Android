package com.ninecraft.booket.feature.record.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.designsystem.component.RecordProgressBar
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.step.EmotionStep
import com.ninecraft.booket.feature.record.step.ImpressionStep
import com.ninecraft.booket.feature.record.step.QuoteStep
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordRegister(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        state.eventSink(RecordRegisterUiEvent.OnBackButtonClick)
    }

    ReedFullScreen(
        modifier = modifier.fillMaxSize(),
    ) {
        ReedBackTopAppBar(
            onBackClick = {
                state.eventSink(RecordRegisterUiEvent.OnBackButtonClick)
            },
        )
        RecordProgressBar(
            currentStep = state.currentStep,
            modifier = modifier.padding(horizontal = ReedTheme.spacing.spacing5),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        when (state.currentStep) {
            RecordStep.QUOTE -> {
                QuoteStep(state = state)
            }

            RecordStep.EMOTION -> {
                EmotionStep(state = state)
            }

            RecordStep.IMPRESSION -> {
                ImpressionStep(state = state)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        ReedButton(
            onClick = {
                state.eventSink(RecordRegisterUiEvent.OnNextButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReedTheme.spacing.spacing5),
            text = stringResource(R.string.record_next_button),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
    }

    if (state.isExitDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.record_exit_dialog_title),
            description = stringResource(R.string.record_exit_dialog_description),
            confirmButtonText = stringResource(R.string.record_exit_dialog_confirm),
            dismissButtonText = stringResource(R.string.record_exit_dialog_dismiss),
            onConfirmRequest = {
                state.eventSink(RecordRegisterUiEvent.OnExitDialogConfirm)
            },
            onDismissRequest = {
                state.eventSink(RecordRegisterUiEvent.OnExitDialogDismiss)
            },
        )
    }

    if (state.isRecordSavedDialogVisible) {
        ReedDialog(
            title = "기록이 저장되었어요!",
            description = "방금 남긴 기록을 확인해볼까요?",
            confirmButtonText = "기록 보러가기",
            dismissButtonText = "닫기",
            onConfirmRequest = {
                state.eventSink(RecordRegisterUiEvent.OnRecordSavedDialogConfirm)
            },
            onDismissRequest = {
                state.eventSink(RecordRegisterUiEvent.OnRecordSavedDialogDismiss)
            }
        )
    }
}

@DevicePreview
@Composable
private fun RecordRegisterPreview() {
    ReedTheme {
        RecordRegister(
            state = RecordRegisterUiState(
                eventSink = {},
            ),
        )
    }
}
