package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun RecordItem(
    quote: String,
    emotionTags: ImmutableList<String>,
    pageNumber: Int,
    createdAt: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(ReedTheme.radius.md))
            .background(ReedTheme.colors.baseSecondary)
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing5,
                end = ReedTheme.spacing.spacing5,
                bottom = ReedTheme.spacing.spacing4,
            ),
    ) {
        Text(
            text = "{$quote}",
            color = ReedTheme.colors.contentSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 4,
            style = ReedTheme.typography.body2Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(getEmotionImageResourceByDisplayName(emotionTags[0])),
                contentDescription = "Emotion Graphic",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            Column {
                Text(
                    text = "#${emotionTags[0]}",
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.label1SemiBold,
                )
                Text(
                    text = createdAt,
                    color = ReedTheme.colors.contentTertiary,
                    style = ReedTheme.typography.caption1Regular,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${pageNumber}P",
                color = ReedTheme.colors.contentBrand,
                style = ReedTheme.typography.body2Medium,
            )
        }
    }
}

private fun getEmotionImageResourceByDisplayName(displayName: String): Int {
    return when (displayName) {
        "따뜻함" -> R.drawable.img_warm
        "즐거움" -> R.drawable.img_joy
        "슬픔" -> R.drawable.img_sad
        "깨달음" -> R.drawable.img_insight
        else -> R.drawable.img_warm
    }
}

@ComponentPreview
@Composable
private fun RecordItemPreview() {
    ReedTheme {
        RecordItem(
            quote = "",
            emotionTags = persistentListOf(),
            pageNumber = 12,
            createdAt = "2025.06.25",
        )
    }
}
