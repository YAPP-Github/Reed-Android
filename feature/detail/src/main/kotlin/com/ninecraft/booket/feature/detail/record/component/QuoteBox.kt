package com.ninecraft.booket.feature.detail.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
internal fun QuoteBox(
    quote: String,
    page: Int,
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
                horizontal = ReedTheme.spacing.spacing5,
                vertical = ReedTheme.spacing.spacing4,
            ),
    ) {
        Column {
            Text(
                text = "\"$quote\"",
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.label1Medium,
            )
            Text(
                text = "${page}p",
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentBrand,
                textAlign = TextAlign.End,
                style = ReedTheme.typography.label1Medium,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun QuoteBoxPreview() {
    ReedTheme {
        QuoteBox(
            quote = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
            page = 99,
        )
    }
}
