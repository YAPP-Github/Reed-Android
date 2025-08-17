package com.ninecraft.booket.feature.detail.record.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.textfield.ReedRecordTextField
import com.ninecraft.booket.core.designsystem.component.textfield.digitOnlyInputTransformation
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.record.component.BookItem
import com.ninecraft.booket.feature.screens.RecordEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(RecordEditScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordEditUi(
    state: RecordEditUiState,
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
            ReedTopAppBar(
                title = "독서 기록 수정",
                startIconRes = designR.drawable.ic_close,
                startIconDescription = "Close Icon",
                startIconOnClick = {
                    state.eventSink(RecordEditUiEvent.OnCloseClick)
                },
            )
            RecordEditContent(state = state)
        }
    }
}

@Composable
private fun ColumnScope.RecordEditContent(state: RecordEditUiState) {
    BookItem(
        imageUrl = "",
        bookTitle = "",
        author = "",
        publisher = "",
    )
    Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = ReedTheme.border.border1,
        color = ReedTheme.colors.dividerSm,
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = ReedTheme.spacing.spacing5)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
        Text(
            text = "책 페이지",
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordPageState,
            recordHintRes = R.string.record_detail_edit,
            inputTransformation = digitOnlyInputTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            lineLimits = TextFieldLineLimits.SingleLine,
//            isError = state.isPageError,
//            errorMessage = stringResource(R.string.quote_step_page_input_error),
            onClear = {
//                state.eventSink(RecordRegisterUiEvent.OnClearClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = "문장 기록",
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordQuoteState,
            recordHintRes = R.string.record_detail_edit,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = "감상평",
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordImpressionState,
            recordHintRes = R.string.record_detail_edit,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "감정",
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "따뜻함",
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
            Icon(
                imageVector = ImageVector.vectorResource(designR.drawable.ic_chevron_right),
                contentDescription = "Chevron Right Icon",
                tint = ReedTheme.colors.contentSecondary,
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing16))
    }
    ReedButton(
        onClick = {},
        text = "저장하기",
        sizeStyle = largeButtonStyle,
        colorStyle = ReedButtonColorStyle.PRIMARY,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = ReedTheme.spacing.spacing5,
                vertical = ReedTheme.spacing.spacing4,
            ),
    )
}

@ComponentPreview
@Composable
private fun RecordEditUiPreview() {
    ReedTheme {
        RecordEditUi(
            state = RecordEditUiState(
                eventSink = {},
            ),
        )
    }
}
