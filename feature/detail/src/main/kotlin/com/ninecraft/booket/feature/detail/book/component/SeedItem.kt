package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.bgColor
import com.ninecraft.booket.core.designsystem.graphicRes
import com.ninecraft.booket.core.designsystem.textColor
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionModel

@Composable
internal fun SeedItem(
    emotion: EmotionModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(ReedTheme.colors.baseSecondary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = emotion.code.graphicRes),
            contentDescription = "Seed Graphic Image",
            modifier = Modifier.size(50.dp),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(ReedTheme.radius.full))
                .background(emotion.code.bgColor)
                .padding(
                    horizontal = ReedTheme.spacing.spacing2,
                    vertical = ReedTheme.spacing.spacing1,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emotion.code.displayName,
                color = emotion.code.textColor,
                style = ReedTheme.typography.label2SemiBold,
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Text(
            text = "${emotion.count}",
            color = ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.label2Regular,
        )
    }
}

@ComponentPreview
@Composable
private fun SeedItemPreview() {
    ReedTheme {
        SeedItem(
            emotion = EmotionModel(
                code = EmotionCode.WARMTH,
                count = 3,
            ),
        )
    }
}
