package com.ninecraft.booket.feature.detail.book.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun BookItem(
    bookDetail: BookDetailModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReedTheme.spacing.spacing5),
    ) {
        NetworkImage(
            imageUrl = bookDetail.cover,
            contentDescription = "Book CoverImage",
            modifier = Modifier
                .padding(end = ReedTheme.spacing.spacing4)
                .width(70.dp)
                .height(99.dp)
                .clip(RoundedCornerShape(size = ReedTheme.radius.xs)),
            placeholder = painterResource(designR.drawable.ic_placeholder),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "여름은 오래 그곳에 남아",
                color = ReedTheme.colors.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = ReedTheme.typography.headline1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "미쓰이에 마사시",
                    color = ReedTheme.colors.contentTertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label2Regular,
                    modifier = Modifier.weight(0.7f, fill = false),
                )
                Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                VerticalDivider(
                    modifier = Modifier.height(14.dp),
                    thickness = 1.dp,
                    color = ReedTheme.colors.contentTertiary,
                )
                Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                Text(
                    text = "비채",
                    color = ReedTheme.colors.contentTertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label2Regular,
                    modifier = Modifier.weight(0.3f, fill = false),
                )
            }
            Spacer(Modifier.width(ReedTheme.spacing.spacing05))
            Text(
                text = "2024년",
                color = ReedTheme.colors.contentTertiary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.label2Regular,
            )
        }
    }
}
