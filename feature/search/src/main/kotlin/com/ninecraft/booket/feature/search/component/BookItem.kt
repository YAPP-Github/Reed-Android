package com.ninecraft.booket.feature.search.component

import androidx.compose.foundation.clickable
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
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun BookItem(
    book: BookSummaryModel,
    onBookClick: (BookSummaryModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBookClick(book) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NetworkImage(
            imageUrl = book.coverImageUrl,
            contentDescription = "Book CoverImage",
            modifier = Modifier
                .padding(
                    top = ReedTheme.spacing.spacing4,
                    end = ReedTheme.spacing.spacing4,
                    bottom = ReedTheme.spacing.spacing4,
                )
                .width(68.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(size = ReedTheme.radius.sm)),
            placeholder = painterResource(designR.drawable.ic_placeholder),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = ReedTheme.spacing.spacing5),
        ) {
            Text(
                text = book.title,
                color = ReedTheme.colors.contentPrimary,
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
                    color = ReedTheme.colors.contentTertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = ReedTheme.typography.label1Medium,
                    modifier = Modifier.weight(1f, fill = false),
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
