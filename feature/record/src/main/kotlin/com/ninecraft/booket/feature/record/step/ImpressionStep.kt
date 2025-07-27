package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.smallRoundedButtonStyle
import com.ninecraft.booket.core.designsystem.component.textfield.ReedRecordTextField
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState

@Composable
fun ImpressionStep(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(White)
            .padding(horizontal = ReedTheme.spacing.spacing5),
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
                .height(140.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        ReedButton(
            onClick = {},
            colorStyle = ReedButtonColorStyle.STROKE,
            sizeStyle = smallRoundedButtonStyle,
            modifier = Modifier.align(Alignment.End),
            text = "감상평 가이드",
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(com.ninecraft.booket.core.designsystem.R.drawable.ic_book_open),
                    contentDescription = "Open Book Icon",
                )
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

