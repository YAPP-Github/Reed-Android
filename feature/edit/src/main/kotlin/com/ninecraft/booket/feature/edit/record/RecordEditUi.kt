package com.ninecraft.booket.feature.edit.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.res.stringResource
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
import com.ninecraft.booket.feature.edit.R
import com.ninecraft.booket.feature.edit.record.component.BookItem
import com.ninecraft.booket.feature.screens.RecordEditScreen
import com.ninecraft.booket.feature.screens.arguments.RecordEditArgs
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
                .padding(innerPadding)
                .imePadding(),
        ) {
            ReedTopAppBar(
                title = stringResource(R.string.edit_record_title),
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
        imageUrl = state.recordInfo.bookCoverImageUrl,
        bookTitle = state.recordInfo.bookTitle,
        author = state.recordInfo.author,
        publisher = state.recordInfo.bookPublisher,
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
            text = stringResource(R.string.edit_record_page_label),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordPageState,
            recordHintRes = R.string.edit_record_page_hint,
            inputTransformation = digitOnlyInputTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            lineLimits = TextFieldLineLimits.SingleLine,
            isError = state.isPageError,
            errorMessage = stringResource(R.string.edit_record_page_input_error),
            onClear = {
                state.eventSink(RecordEditUiEvent.OnClearClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = stringResource(R.string.edit_record_quote_label),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordQuoteState,
            recordHintRes = R.string.edit_record_quote_hint,
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
            text = stringResource(R.string.edit_record_impression_label),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        ReedRecordTextField(
            recordState = state.recordImpressionState,
            recordHintRes = R.string.edit_record_impression_hint,
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
                text = stringResource(R.string.edit_record_emotion_label),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.clickable {
                    state.eventSink(RecordEditUiEvent.OnEmotionEditClick)
                },
            ) {
                val emotion = state.recordInfo.emotionTags.firstOrNull() ?: ""

                Text(
                    text = emotion,
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
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing16))
    }
    ReedButton(
        onClick = {},
        text = stringResource(R.string.edit_record_save),
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
                recordInfo = RecordEditArgs(
                    id = "",
                    userBookId = "",
                    pageNumber = 33,
                    quote = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                    review = "감동적이었다.",
                    emotionTags = listOf("따뜻함"),
                    bookTitle = "여름은 오래 그곳에 남아",
                    bookPublisher = "비채",
                    bookCoverImageUrl = "",
                    author = "마쓰이에 마사시",
                ),
                eventSink = {},
            ),
        )
    }
}
