package com.ninecraft.booket.feature.record.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.designsystem.component.RecordProgressBar
import com.ninecraft.booket.core.designsystem.component.ReedDialog
import com.ninecraft.booket.core.designsystem.component.textfield.ReedRecordTextField
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.button.smallRoundedButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordRegister(
    state: RecordUiState,
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
        RecordRegisterContent(state = state)
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
}

@Composable
internal fun RecordRegisterContent(
    state: RecordUiState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = ReedTheme.spacing.spacing5),
    ) {
        RecordProgressBar(
            currentStep = RecordStep.REGISTER,
            modifier = modifier,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        Text(
            text = stringResource(R.string.record_register_title1),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.heading1Bold,
        )
        Text(
            text = stringResource(R.string.record_register_title2),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.heading1Bold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        Text(
            text = stringResource(R.string.record_page_label),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordPageState,
            recordHintRes = R.string.record_page_hint,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            onClear = {
                state.eventSink(RecordRegisterUiEvent.OnClearClick)
            },
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = stringResource(R.string.record_sentence_label),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordSentenceState,
            recordHintRes = R.string.record_sentence_hint,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.STROKE,
            sizeStyle = smallRoundedButtonStyle,
            modifier = Modifier.align(Alignment.End),
            text = stringResource(R.string.record_scan_sentence),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_maximize),
                    contentDescription = "Scan Icon",
                )
            },
        )
        Spacer(modifier = Modifier.weight(1f))
        ReedButton(
            onClick = {
                state.eventSink(RecordRegisterUiEvent.OnNextButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.record_next_button),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
    }
}

@DevicePreview
@Composable
private fun RecordRegisterPreview() {
    ReedTheme {
        RecordRegister(
            state = RecordUiState(
                eventSink = {},
            ),
        )
    }
}
