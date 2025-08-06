package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.register.RecordRegisterUiEvent
import com.ninecraft.booket.feature.record.register.RecordRegisterUiState
import kotlinx.collections.immutable.toPersistentList

@Composable
fun EmotionStep(
    state: RecordRegisterUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(White)
            .padding(horizontal = ReedTheme.spacing.spacing5),
    ) {
        Text(
            text = stringResource(R.string.emotion_step_title),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.heading1Bold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Text(
            text = stringResource(R.string.emotion_step_description),
            color = ReedTheme.colors.contentTertiary,
            style = ReedTheme.typography.label1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing10))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
            horizontalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing3),
            content = {
                items(state.emotionTags) { tag ->
                    EmotionItem(
                        emotionTag = tag,
                        onClick = {
                            state.eventSink(RecordRegisterUiEvent.OnSelectEmotion(tag))
                        },
                        isSelected = state.selectedEmotion == tag,
                    )
                }
            },
        )
    }
}

@Composable
private fun EmotionItem(
    emotionTag: EmotionTag,
    onClick: () -> Unit,
    isSelected: Boolean,
) {
    Box(
        modifier = Modifier
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
private fun RecordRegisterPreview() {
    val emotionTags = EmotionTag.entries.toPersistentList()

    ReedTheme {
        EmotionStep(
            state = RecordRegisterUiState(
                emotionTags = emotionTags,
                eventSink = {},
            ),
        )
    }
}
