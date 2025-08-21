package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.RecordStep
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.button.smallRoundedButtonStyle
import com.ninecraft.booket.core.designsystem.component.textfield.ReedRecordTextField
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.component.ImpressionGuideBottomSheet
import com.ninecraft.booket.feature.record.register.RecordRegisterUiEvent
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState
import kotlinx.coroutines.launch
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpressionStep(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val impressionGuideBottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        if (state.impressionState.text.isEmpty()) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ReedTheme.spacing.spacing5)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(R.string.impression_step_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.heading1Bold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            Text(
                text = stringResource(R.string.impression_step_description),
                color = ReedTheme.colors.contentTertiary,
                style = ReedTheme.typography.label1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
            ReedRecordTextField(
                recordState = state.impressionState,
                recordHintRes = R.string.impression_step_hint,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .height(140.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default,
                ),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            ReedButton(
                onClick = {
                    state.eventSink(RecordRegisterUiEvent.OnImpressionGuideButtonClick)
                },
                colorStyle = ReedButtonColorStyle.STROKE,
                sizeStyle = smallRoundedButtonStyle,
                modifier = Modifier.align(Alignment.End),
                text = stringResource(R.string.impression_step_guide),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(designR.drawable.ic_book_open),
                        contentDescription = "Impression Guide Icon",
                    )
                },
            )
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

    if (state.isImpressionGuideBottomSheetVisible) {
        ImpressionGuideBottomSheet(
            onDismissRequest = {
                state.eventSink(RecordRegisterUiEvent.OnImpressionGuideBottomSheetDismiss)
            },
            sheetState = impressionGuideBottomSheetState,
            impressionState = state.impressionState,
            impressionGuideList = state.impressionGuideList,
            beforeSelectedImpressionGuide = state.beforeSelectedImpressionGuide,
            selectedImpressionGuide = state.selectedImpressionGuide,
            onGuideClick = {
                state.eventSink(RecordRegisterUiEvent.OnSelectImpressionGuide(it))
            },
            onCloseButtonClick = {
                coroutineScope.launch {
                    impressionGuideBottomSheetState.hide()
                    state.eventSink(RecordRegisterUiEvent.OnImpressionGuideBottomSheetDismiss)
                }
            },
            onSelectionConfirmButtonClick = {
                coroutineScope.launch {
                    impressionGuideBottomSheetState.hide()
                    state.eventSink(RecordRegisterUiEvent.OnImpressionGuideConfirmed)
                }
            },
        )
    }
}

@ComponentPreview
@Composable
private fun ImpressionStepPreview() {
    ReedTheme {
        ImpressionStep(
            state = RecordRegisterUiState(
                eventSink = {},
            ),
        )
    }
}
