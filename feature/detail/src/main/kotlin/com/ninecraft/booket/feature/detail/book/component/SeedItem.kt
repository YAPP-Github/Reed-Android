package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.toBackgroundColor
import com.ninecraft.booket.core.common.extensions.toTextColor
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.ResourceImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun SeedItem(
    emotion: EmotionModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(ReedTheme.colors.baseSecondary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ResourceImage(
            imageRes = designR.drawable.ic_placeholder,
            contentDescription = "Seed Graphic Image",
            modifier = Modifier.size(50.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(ReedTheme.radius.full))
                .background(emotion.type.toBackgroundColor())
                .padding(
                    horizontal = ReedTheme.spacing.spacing2,
                    vertical = ReedTheme.spacing.spacing1,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emotion.type.displayName,
                color = emotion.type.toTextColor(),
                style = ReedTheme.typography.body2Medium,
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Text(
            text = "${emotion.count}",
            color = ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.body2Medium,
        )
    }
}

@ComponentPreview
@Composable
private fun SeedItemPreview() {
    ReedTheme {
        SeedItem(
            emotion = EmotionModel(
                type = Emotion.WARM,
                count = 3,
            ),
        )
    }
}
