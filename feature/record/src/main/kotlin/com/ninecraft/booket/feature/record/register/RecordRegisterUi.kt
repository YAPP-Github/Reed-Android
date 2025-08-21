package com.ninecraft.booket.feature.record.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.designsystem.component.RecordProgressBar
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.step.EmotionStep
import com.ninecraft.booket.feature.record.step.ImpressionStep
import com.ninecraft.booket.feature.record.step.QuoteStep
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordRegisterUi(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    HandleRecordRegisterSideEffects(state = state)

    BackHandler {
        state.eventSink(RecordRegisterUiEvent.OnBackButtonClick)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.ime),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedBackTopAppBar(
                onBackClick = {
                    state.eventSink(RecordRegisterUiEvent.OnBackButtonClick)
                },
            )
            RecordProgressBar(
                currentStep = state.currentStep,
                modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
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
        }
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
            title = stringResource(R.string.record_saved_dialog_title),
            description = stringResource(R.string.record_saved_dialog_description),
            confirmButtonText = stringResource(R.string.record_saved_dialog_move_to_detail),
            dismissButtonText = stringResource(R.string.record_saved_dialog_close),
            onConfirmRequest = {
                state.eventSink(RecordRegisterUiEvent.OnRecordSavedDialogConfirm(state.savedRecordId))
            },
            onDismissRequest = {
                state.eventSink(RecordRegisterUiEvent.OnRecordSavedDialogDismiss)
            },
            headerContent = {
                Image(
                    painter = painterResource(R.drawable.img_record_complete),
                    contentDescription = "Record Complete Image",
                    modifier = Modifier.height(132.dp),
                )
            },
        )
    }
}

@DevicePreview
@Composable
private fun RecordRegisterPreview() {
    ReedTheme {
        RecordRegisterUi(
            state = RecordRegisterUiState(
                eventSink = {},
            ),
        )
    }
}
