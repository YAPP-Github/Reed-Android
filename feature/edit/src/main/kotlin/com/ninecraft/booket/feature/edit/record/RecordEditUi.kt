package com.ninecraft.booket.feature.edit.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.edit.R
import com.ninecraft.booket.feature.edit.record.component.BookItem
import com.ninecraft.booket.feature.edit.record.component.EmotionItem
import com.ninecraft.booket.feature.screens.RecordEditScreen
import com.ninecraft.booket.feature.screens.arguments.DetailEmotionArg
import com.ninecraft.booket.feature.screens.arguments.PrimaryEmotionArg
import com.ninecraft.booket.feature.screens.arguments.RecordEditArgs
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.collections.immutable.toPersistentList
import com.ninecraft.booket.core.designsystem.R as designR

@TraceRecomposition
@CircuitInject(RecordEditScreen::class, AppScope::class)
@Composable
internal fun RecordEditUi(
    state: RecordEditUiState,
    modifier: Modifier = Modifier,
) {
    HandleRecordEditSideEffects(
        state = state,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.ime),
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

@TraceRecomposition
@Composable
private fun ColumnScope.RecordEditContent(state: RecordEditUiState) {
    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
    ) {
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
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ReedTheme.spacing.spacing5),
        ) {
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
                text = stringResource(R.string.edit_record_memo_label),
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
            Text(
                text = stringResource(R.string.edit_record_emotion_label),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.body1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            EmotionItem(
                primaryEmotionCode = state.recordInfo.primaryEmotion.code,
                primaryEmotionName = state.recordInfo.primaryEmotion.displayName,
                detailEmotions = state.recordInfo.detailEmotions.toPersistentList(),
                onClick = {
                    state.eventSink(RecordEditUiEvent.OnEmotionEditClick)
                },
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing16))
        }
    }
    ReedButton(
        onClick = {
            state.eventSink(RecordEditUiEvent.OnSaveButtonClick)
        },
        text = stringResource(R.string.edit_record_save),
        sizeStyle = largeButtonStyle,
        colorStyle = ReedButtonColorStyle.PRIMARY,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = ReedTheme.spacing.spacing5,
                vertical = ReedTheme.spacing.spacing4,
            ),
        enabled = state.isSaveButtonEnabled,
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
                    pageNumber = 33,
                    quote = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                    review = "감동적이었다.",
                    primaryEmotion = PrimaryEmotionArg(
                        code = EmotionCode.WARMTH,
                        displayName = "따뜻함",
                    ),
                    detailEmotions = listOf(
                        DetailEmotionArg(
                            id = "84f95d93-e54c-11f0-8545-525ae7dd628c",
                            name = "위로받은",
                        ),
                        DetailEmotionArg(
                            id = "84f95e7e-e54c-11f0-8545-525ae7dd628c",
                            name = "포근한",
                        ),
                    ),
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
