package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import com.ninecraft.booket.core.common.extensions.toFormattedDate
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.feature.detail.R
import kotlinx.collections.immutable.persistentListOf
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun RecordItem(
    recordInfo: ReadingRecordModel,
    onRecordMenuClick: (ReadingRecordModel) -> Unit,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(getEmotionImageResourceByDisplayName(recordInfo.emotionTags[0])),
                contentDescription = "Emotion Graphic",
                modifier = Modifier
                    .size(ReedTheme.spacing.spacing8)
                    .clip(CircleShape)
                    .background(ReedTheme.colors.basePrimary),
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            Text(
                text = "#${recordInfo.emotionTags[0]}",
                color = ReedTheme.colors.contentBrand,
                style = ReedTheme.typography.body1SemiBold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(designR.drawable.ic_more_vertical),
                contentDescription = "More Vertical Icon",
                modifier = Modifier
                    .size(ReedTheme.spacing.spacing5)
                    .clickable {
                        onRecordMenuClick(recordInfo)
                    },
                tint = ReedTheme.colors.contentTertiary,
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        Text(
            text = "\"${recordInfo.quote}\"",
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
            Text(
                text = recordInfo.createdAt.toFormattedDate(),
                color = ReedTheme.colors.contentTertiary,
                style = ReedTheme.typography.label1Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${recordInfo.pageNumber}p",
                color = ReedTheme.colors.contentTertiary,
                style = ReedTheme.typography.body2Medium,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

fun getEmotionImageResourceByDisplayName(displayName: String): Int {
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
            recordInfo = ReadingRecordModel(
                quote = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                emotionTags = persistentListOf(),
                pageNumber = 12,
                createdAt = "2025.06.25",
            ),
            onRecordMenuClick = {},
        )
    }
}
