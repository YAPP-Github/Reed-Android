package com.ninecraft.booket.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.component.ResourceImage
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.feature.home.R
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun BookCard(
    recentBookInfo: RecentBookModel,
    onBookDetailClick: () -> Unit,
    onRecordButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
                ambientColor = Color(0xFFBCC4BE).copy(alpha = 0.2f),
                spotColor = Color(0xFFBCC4BE).copy(alpha = 0.2f),
            )
            .background(
                color = ReedTheme.colors.basePrimary,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .clip(shape = RoundedCornerShape(ReedTheme.radius.sm))
            .border(
                width = 1.dp,
                color = ReedTheme.colors.borderSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing6,
                end = ReedTheme.spacing.spacing5,
                bottom = ReedTheme.spacing.spacing5,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        NetworkImage(
            imageUrl = recentBookInfo.coverImageUrl,
            contentDescription = "Book CoverImage",
            modifier = Modifier
                .width(86.dp)
                .height(125.dp)
                .clip(RoundedCornerShape(size = ReedTheme.radius.sm))
                .border(
                    width = 1.dp,
                    color = ReedTheme.colors.borderPrimary,
                    shape = RoundedCornerShape(ReedTheme.radius.sm),
                )
                .noRippleClickable {
                    onBookDetailClick()
                },
            placeholder = painterResource(designR.drawable.ic_placeholder),
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Text(
            text = recentBookInfo.title,
            color = ReedTheme.colors.contentPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = ReedTheme.typography.headline1SemiBold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = recentBookInfo.author,
                color = ReedTheme.colors.contentTertiary,
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
                text = recentBookInfo.publisher,
                color = ReedTheme.colors.contentTertiary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.label1Medium,
                modifier = Modifier.weight(0.3f, fill = false),
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .background(
                        color = ReedTheme.colors.baseSecondary,
                        shape = RoundedCornerShape(ReedTheme.radius.sm),
                    )
                    .clip(shape = RoundedCornerShape(ReedTheme.radius.sm))
                    .clickableSingle {
                        onBookDetailClick()
                    }
                    .padding(
                        horizontal = ReedTheme.spacing.spacing3,
                        vertical = ReedTheme.spacing.spacing2,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(White),
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
                Text(
                    text = buildAnnotatedString {
                        append("${recentBookInfo.recordCount}")
                        withStyle(style = SpanStyle(color = ReedTheme.colors.contentSecondary)) {
                            append("개")
                        }
                    },
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.label1SemiBold,
                )
            }
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            ReedButton(
                onClick = {
                    onRecordButtonClick()
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.book_card_record),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(designR.drawable.ic_edit_3),
                        contentDescription = "Edit Icon",
                    )
                },
            )
        }
    }
}

@Composable
fun EmptyBookCard(
    onBookRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
                ambientColor = Color(0xFFBCC4BE).copy(alpha = 0.2f),
                spotColor = Color(0xFFBCC4BE).copy(alpha = 0.2f),
            )
            .background(
                color = ReedTheme.colors.basePrimary,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .border(
                width = 1.dp,
                color = ReedTheme.colors.borderSecondary,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .padding(
                start = ReedTheme.spacing.spacing5,
                top = ReedTheme.spacing.spacing6,
                end = ReedTheme.spacing.spacing5,
                bottom = ReedTheme.spacing.spacing5,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        ResourceImage(
            imageRes = R.drawable.img_empty_book,
            contentDescription = "Empty Book",
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Text(
            text = stringResource(R.string.empty_book_card_title),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.headline1SemiBold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
        Text(
            text = stringResource(R.string.empty_book_card_description),
            color = ReedTheme.colors.contentTertiary,
            style = ReedTheme.typography.label1Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        ReedButton(
            onClick = {
                onBookRegisterClick()
            },
            sizeStyle = largeButtonStyle,
            colorStyle = ReedButtonColorStyle.PRIMARY,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.empty_book_card_register),
        )
    }
}

@ComponentPreview
@Composable
private fun BookCardPreview() {
    ReedTheme {
        BookCard(
            recentBookInfo = RecentBookModel(
                title = "여름은 오래 그곳에 남아",
                author = "마쓰이에 마사시",
                publisher = "비채",
                coverImageUrl = "https://image.aladin.co.kr/product/7492/9/cover200/8934972203_1.jpg"
            ),
            onBookDetailClick = {},
            onRecordButtonClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun EmptyBookCardPreview() {
    ReedTheme {
        EmptyBookCard(
            onBookRegisterClick = {},
        )
    }
}
