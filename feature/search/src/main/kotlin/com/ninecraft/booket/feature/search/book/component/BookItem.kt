package com.ninecraft.booket.feature.search.book.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.feature.search.R
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun BookItem(
    book: BookSummaryModel,
    onBookClick: (BookSummaryModel) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val titleColor = if (enabled) ReedTheme.colors.contentPrimary else ReedTheme.colors.contentDisabled
    val authorColor = if (enabled) ReedTheme.colors.contentTertiary else ReedTheme.colors.contentDisabled
    val bgColor = if (enabled) White else ReedTheme.colors.bgDisabled

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .then(
                if (enabled) Modifier.clickable { onBookClick(book) } else Modifier,
            )
            .padding(horizontal = ReedTheme.spacing.spacing5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = ReedTheme.spacing.spacing4,
                    end = ReedTheme.spacing.spacing4,
                    bottom = ReedTheme.spacing.spacing4,
                )
                .width(68.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(size = ReedTheme.radius.sm)),
        ) {
            NetworkImage(
                imageUrl = book.coverImageUrl,
                contentDescription = "Book CoverImage",
                modifier = Modifier.matchParentSize(),
                placeholder = painterResource(designR.drawable.ic_placeholder),
            )

            if (!enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            if (!enabled) {
                Text(
                    text = stringResource(R.string.book_status_registered),
                    color = ReedTheme.colors.contentSuccess,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label2Regular,
                )
                Spacer(Modifier.height(ReedTheme.spacing.spacing1))
            }
            Text(
                text = book.title,
                color = titleColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.body1SemiBold,
            )
            Spacer(Modifier.height(ReedTheme.spacing.spacing1))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = book.author,
                    color = authorColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label1Medium,
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
                    text = book.publisher,
                    color = authorColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label1Medium,
                    modifier = Modifier.weight(0.3f, fill = false),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BookItemPreview() {
    ReedTheme {
        BookItem(
            book = BookSummaryModel(
                title = "여름은 오래 그곳에 남아",
                author = "마쓰이에 마사시 마쓰이에 마사시",
                publisher = "비채",
                coverImageUrl = "https://example.com/sample-book-cover.jpg",
                isbn = "",
            ),
            onBookClick = {},
        )
    }
}
