package com.ninecraft.booket.feature.record.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.button.smallRoundedButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.RecordStep
import com.ninecraft.booket.feature.record.component.RecordProgressBar
import com.ninecraft.booket.feature.screens.RecordScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(RecordScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordRegister(
    state: RecordUiState,
    modifier: Modifier = Modifier,
) {
    BackHandler {}

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
}

@Composable
internal fun RecordRegisterContent(
    state: RecordUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        RecordProgressBar(
            step = RecordStep.REGISTER.value,
            modifier = modifier,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        Text(
            text = stringResource(R.string.record_register_title1),
        )
        Text(
            text = stringResource(R.string.record_register_title2),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        Text(
            text = stringResource(R.string.record_page_label),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = stringResource(R.string.record_sentence_label),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.STROKE,
            sizeStyle = smallRoundedButtonStyle,
        ) {
            Text(
                text = stringResource(R.string.record_scan_sentence),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ReedButton(
            onClick = {
                state.eventSink(RecordRegisterUiEvent.OnNextButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.record_next_button),
            )
        }
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
