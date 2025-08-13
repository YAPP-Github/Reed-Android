package com.ninecraft.booket.feature.search.library.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.feature.search.R
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun LibraryBookItem(
    book: LibraryBookSummaryModel,
    onBookClick: (LibraryBookSummaryModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSingle { onBookClick(book) }
            .padding(horizontal = ReedTheme.spacing.spacing5),
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.bookTitle,
                color = ReedTheme.colors.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.body1SemiBold,
            )
            Spacer(Modifier.height(ReedTheme.spacing.spacing1))
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val authorMaxWidth = maxWidth * 0.7f

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = book.bookAuthor,
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
                        text = book.publisher,
                        color = ReedTheme.colors.contentTertiary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = ReedTheme.typography.label1Medium,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                }
            }
            Spacer(Modifier.height(ReedTheme.spacing.spacing4))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.library_book_item_records),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.label2Regular,
                )
                Spacer(Modifier.width(ReedTheme.spacing.spacing1))
                Text(
                    text = book.recordCount.toString(),
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.label2SemiBold,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun LibraryBookItemPreview() {
    ReedTheme {
        LibraryBookItem(
            book = LibraryBookSummaryModel(
                bookTitle = "여름은 오래 그곳에 남아",
                bookAuthor = "마쓰이에 마사시 마쓰이에 마사시",
                publisher = "비채",
                coverImageUrl = "https://example.com/sample-book-cover.jpg",
                recordCount = 3,
            ),
            onBookClick = {},
        )
    }
}
