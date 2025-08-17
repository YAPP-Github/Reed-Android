package com.ninecraft.booket.feature.detail.record.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.detail.book.component.getEmotionImageResourceByDisplayName

@Composable
internal fun ReviewBox(
    emotion: String,
    createdAt: String,
    review: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ReedTheme.colors.baseSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.md),
            )
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing3,
            ),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(getEmotionImageResourceByDisplayName(emotion)),
                    contentDescription = "Emotion Graphic",
                    modifier = Modifier
                        .size(ReedTheme.spacing.spacing10)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
                Text(
                    text = "#$emotion",
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.body2Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = createdAt,
                    color = ReedTheme.colors.contentTertiary,
                    style = ReedTheme.typography.label2Regular,
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Text(
                text = review,
                color = ReedTheme.colors.contentSecondary,
                style = ReedTheme.typography.label1Medium,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ReviewBoxPreview() {
    ReedTheme {
        ReviewBox(
            emotion = "따뜻함",
            review = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다",
            createdAt = "2025.06.25",
        )
    }
}
