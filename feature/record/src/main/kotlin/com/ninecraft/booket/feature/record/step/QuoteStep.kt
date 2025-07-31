package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.smallRoundedButtonStyle
import com.ninecraft.booket.core.designsystem.component.textfield.ReedRecordTextField
import com.ninecraft.booket.core.designsystem.component.textfield.digitOnlyInputTransformation
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.register.RecordRegisterUiEvent
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun QuoteStep(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = modifier
            .background(White)
            .imePadding()
            .padding(horizontal = ReedTheme.spacing.spacing5),
    ) {
        item {
            Text(
                text = stringResource(R.string.quote_step_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.heading1Bold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
            Text(
                text = stringResource(R.string.quote_step_page_label),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            ReedRecordTextField(
                recordState = state.recordPageState,
                recordHintRes = R.string.quote_step_page_hint,
                inputTransformation = digitOnlyInputTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                lineLimits = TextFieldLineLimits.SingleLine,
                isError = state.isPageError,
                errorMessage = stringResource(R.string.quote_step_page_input_error),
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
                text = stringResource(R.string.quote_step_sentence_label),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            ReedRecordTextField(
                recordState = state.recordSentenceState,
                recordHintRes = R.string.quote_step_sentence_hint,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                ReedButton(
                    onClick = {
                        state.eventSink(RecordRegisterUiEvent.OnSentenceScanButtonClick)
                    },
                    colorStyle = ReedButtonColorStyle.STROKE,
                    sizeStyle = smallRoundedButtonStyle,
                    text = stringResource(R.string.quote_step_scan_sentence),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(designR.drawable.ic_maximize),
                            contentDescription = "Scan Icon",
                        )
                    },
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun QuoteStepPreview() {
    ReedTheme {
        QuoteStep(
            state = RecordRegisterUiState(
                eventSink = {},
            ),
        )
    }
}
