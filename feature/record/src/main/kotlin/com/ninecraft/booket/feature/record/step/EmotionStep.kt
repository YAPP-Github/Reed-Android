package com.ninecraft.booket.feature.record.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.record.R

@Composable
fun EmotionStep(
    modifier: Modifier = Modifier,
) {
    val emotionList = listOf("기쁨", "슬픔", "분노", "놀람")
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
                items(emotionList) { title ->
                    EmotionItem(title)
                }
            },
        )
    }
}

@Composable
private fun EmotionItem(title: String) {
    Box(
        modifier = Modifier
            .height(214.dp)
            .background(
                color = ReedTheme.colors.bgSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = title)
    }
}

@ComponentPreview
@Composable
private fun RecordRegisterPreview() {
    ReedTheme {
        EmotionStep()
    }
}
