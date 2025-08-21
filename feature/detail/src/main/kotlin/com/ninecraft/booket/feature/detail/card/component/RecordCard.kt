package com.ninecraft.booket.feature.detail.card.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.EmotionTag
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.R

@Composable
internal fun RecordCard(
    quote: String,
    bookTitle: String,
    emotionTag: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(448.dp),
    ) {
        Image(
            painter = painterResource(getEmotionCardImage(emotionTag)),
            contentDescription = "Record Card Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ReedTheme.spacing.spacing8),
        ) {
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing16))
            Text(
                text = quote,
                style = ReedTheme.typography.quoteMedium,
                color = ReedTheme.colors.contentPrimary,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "『$bookTitle』",
                    style = ReedTheme.typography.quoteMedium,
                    color = ReedTheme.colors.contentPrimary,
                )
            }
        }
    }
}

private fun getEmotionCardImage(emotionTag: String): Int {
    return when (emotionTag) {
        EmotionTag.WARMTH.label -> R.drawable.img_record_card_warm
        EmotionTag.JOY.label -> R.drawable.img_record_card_joy
        EmotionTag.SADNESS.label -> R.drawable.img_record_card_sad
        EmotionTag.INSIGHT.label -> R.drawable.img_record_card_insight
        else -> R.drawable.img_record_card_warm
    }
}

@ComponentPreview
@Composable
private fun RecordCardPreview() {
    ReedTheme {
        RecordCard(
            quote = "이 세상에 집이라 이름 붙일 수 없는 것이 있다면 그건 바로 여기, 내가 앉아 있는 이곳일 것이다.",
            bookTitle = "샤이닝",
            emotionTag = EmotionTag.WARMTH.label,
        )
    }
}
