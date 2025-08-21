package com.ninecraft.booket.feature.detail.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.captureToGraphicsLayer
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedTopAppBar
import com.ninecraft.booket.feature.detail.R
import com.ninecraft.booket.feature.detail.card.component.RecordCard
import com.ninecraft.booket.feature.screens.RecordCardScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(RecordCardScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun RecordCardUi(
    state: RecordCardUiState,
    modifier: Modifier = Modifier,
) {
    val recordCardGraphicsLayer = rememberGraphicsLayer()

    HandleRecordCardSideEffects(
        state = state,
        recordCardGraphicsLayer = recordCardGraphicsLayer,
        eventSink = state.eventSink,
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedTopAppBar(
                startIconRes = designR.drawable.ic_chevron_left,
                startIconDescription = "Back Icon",
                startIconOnClick = {
                    state.eventSink(RecordCardUiEvent.OnBackClick)
                },
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = ReedTheme.spacing.spacing5)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RecordCard(
                    quote = state.quote,
                    bookTitle = state.bookTitle,
                    emotionTag = state.emotionTag,
                    modifier = Modifier
                        .padding(
                            top = ReedTheme.spacing.spacing5,
                            start = ReedTheme.spacing.spacing5,
                            end = ReedTheme.spacing.spacing5,
                        )
                        .captureToGraphicsLayer(recordCardGraphicsLayer),
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
                Text(
                    text = stringResource(R.string.share_impressive_quote),
                    color = ReedTheme.colors.contentSecondary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.label1Medium,
                )
                Spacer(modifier = Modifier.height(50.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = ReedTheme.spacing.spacing5,
                        end = ReedTheme.spacing.spacing5,
                        bottom = ReedTheme.spacing.spacing4,
                    ),
            ) {
                ReedButton(
                    onClick = {
                        state.eventSink(RecordCardUiEvent.OnSaveClick)
                    },
                    text = stringResource(R.string.save_image),
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.SECONDARY,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_save),
                            contentDescription = "Save Icon",
                            tint = Color.Unspecified,
                        )
                    },
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                ReedButton(
                    onClick = {
                        state.eventSink(RecordCardUiEvent.OnShareClick)
                    },
                    text = stringResource(R.string.share_card),
                    sizeStyle = largeButtonStyle,
                    colorStyle = ReedButtonColorStyle.PRIMARY,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                            contentDescription = "Share Icon",
                            tint = Color.Unspecified,
                        )
                    },
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun RecordCardUiPreview() {
    ReedTheme {
        RecordCardUi(
            state = RecordCardUiState(
                eventSink = {},
            ),
        )
    }
}
