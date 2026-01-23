package com.ninecraft.booket.feature.record.ocr.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.OcrUiEvent
import com.ninecraft.booket.feature.record.ocr.OcrUiState
import com.ninecraft.booket.feature.record.ocr.component.SentenceBox
import com.skydoves.compose.stability.runtime.TraceRecomposition

@TraceRecomposition
@Composable
internal fun OcrResultContent(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedCloseTopAppBar(
                title = stringResource(R.string.ocr_sentence_selection),
                onClose = {
                    state.eventSink(OcrUiEvent.OnReCaptureButtonClick)
                },
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = ReedTheme.spacing.spacing5),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                item {
                    Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
                }

                items(state.sentenceList.size) { index ->
                    SentenceBox(
                        onClick = {
                            state.eventSink(OcrUiEvent.OnSentenceSelected(index))
                        },
                        sentence = state.sentenceList[index],
                        isSelected = state.selectedIndices.contains(index),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ReedTheme.spacing.spacing5,
                        vertical = ReedTheme.spacing.spacing4,
                    ),
            ) {
                ReedButton(
                    onClick = {
                        state.eventSink(OcrUiEvent.OnReCaptureButtonClick)
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.SECONDARY,
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.ocr_recapture),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                ReedButton(
                    onClick = {
                        state.eventSink(OcrUiEvent.OnSelectionConfirmed)
                    },
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    enabled = state.selectedIndices.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.ocr_selection_confirm),
                )
            }
        }
    }

    if (state.isRecaptureDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.recapture_dialog_title),
            description = stringResource(R.string.recapture_dialog_description),
            confirmButtonText = stringResource(R.string.recapture_dialog_confirm),
            onConfirmRequest = {
                state.eventSink(OcrUiEvent.OnRecaptureDialogConfirmed)
            },
            dismissButtonText = stringResource(R.string.recapture_dialog_cancel),
            onDismissRequest = {
                state.eventSink(OcrUiEvent.OnRecaptureDialogDismissed)
            },
        )
    }
}

@ComponentPreview
@Composable
private fun OcrResultContentPreview() {
    ReedTheme {
        OcrResultContent(
            state = OcrUiState(
                eventSink = {},
            ),
        )
    }
}
