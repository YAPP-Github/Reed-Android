package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.feature.edit.R
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toPersistentList

@CircuitInject(EmotionEditScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun EmotionEditUi(
    state: EmotionEditUiState,
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
            ReedBackTopAppBar(
                onBackClick = {
                    state.eventSink(EmotionEditUiEvent.OnBackClick)
                },
            )
            EmotionEditContent(state = state)
        }
    }
}

@Composable
private fun EmotionEditContent(
    state: EmotionEditUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing4,
                end = ReedTheme.spacing.spacing5,
            ),
    ) {
        Text(
            text = stringResource(R.string.edit_emotion_title),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.heading1Bold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Text(
            text = stringResource(R.string.edit_emotion_description),
            color = ReedTheme.colors.contentTertiary,
            style = ReedTheme.typography.label1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
            horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
            content = {
                items(state.emotionTags) { tag ->
                    EmotionItem(
                        emotionTag = tag,
                        onClick = {
                            state.eventSink(EmotionEditUiEvent.OnSelectEmotion(tag.label))
                        },
                        isSelected = state.selectedEmotion == tag.label,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        )
        ReedButton(
            onClick = {
                state.eventSink(EmotionEditUiEvent.OnEditButtonClick)
            },
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ReedTheme.spacing.spacing4),
            enabled = state.isEditButtonEnabled,
            text = stringResource(R.string.edit_emotion_edit),
        )
    }
}

@Composable
private fun EmotionItem(
    emotionTag: EmotionTag,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(214.dp)
            .background(
                color = ReedTheme.colors.bgTertiary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            )
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    color = ReedTheme.colors.borderBrand,
                    shape = RoundedCornerShape(ReedTheme.radius.md),
                )
                else Modifier,
            )
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .clickableSingle {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(emotionTag.graphic),
            contentDescription = "Emotion Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@ComponentPreview
@Composable
private fun EmotionEditUiPreview() {
    ReedTheme {
        val emotionTags = EmotionTag.entries.toPersistentList()

        EmotionEditUi(
            state = EmotionEditUiState(
                emotionTags = emotionTags,
                eventSink = {},
            ),
        )
    }
}
