package com.ninecraft.booket.feature.detail.record.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun BookItem(
    imageUrl: String,
    bookTitle: String,
    author: String,
    publisher: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = ReedTheme.spacing.spacing5,
                vertical = ReedTheme.spacing.spacing4,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            contentDescription = "Book CoverImage",
            modifier = Modifier
                .padding(end = ReedTheme.spacing.spacing4)
                .width(46.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(size = ReedTheme.radius.xs)),
            placeholder = painterResource(R.drawable.ic_placeholder),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = bookTitle,
                color = ReedTheme.colors.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.body1SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val authorMaxWidth = maxWidth * 0.7f

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = author,
                        color = ReedTheme.colors.contentTertiary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = ReedTheme.typography.label1Medium,
                        modifier = Modifier.widthIn(max = authorMaxWidth),
                    )
                    Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                    VerticalDivider(
                        modifier = Modifier.height(14.dp),
                        thickness = 1.dp,
                        color = ReedTheme.colors.contentTertiary,
                    )
                    Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                    Text(
                        text = publisher,
                        color = ReedTheme.colors.contentTertiary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = ReedTheme.typography.label1Medium,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BookItemPreview() {
    ReedTheme {
        BookItem(
            imageUrl = "",
            bookTitle = "여름은 오래 그곳에 남아",
            author = "마쓰이에 마사시",
            publisher = "비채",
        )
    }
}
