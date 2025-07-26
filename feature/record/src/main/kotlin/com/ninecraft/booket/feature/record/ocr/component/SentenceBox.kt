package com.ninecraft.booket.feature.record.ocr.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun SentenceBox(
    onClick: (String) -> Unit,
    sentence: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val bgColor = if (isSelected) ReedTheme.colors.bgTertiary else ReedTheme.colors.bgSecondary
    val borderColor = if (isSelected) ReedTheme.colors.borderBrand else Color.Transparent
    val textColor = if (isSelected) ReedTheme.colors.contentBrand else ReedTheme.colors.contentPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(ReedTheme.radius.sm),
            )
            .clip(RoundedCornerShape(ReedTheme.radius.sm))
            .clickableSingle {
                onClick(sentence)
            }
            .padding(
                horizontal = ReedTheme.spacing.spacing4,
                vertical = ReedTheme.spacing.spacing3,
            ),
    ) {
        Text(
            text = sentence,
            color = textColor,
            style = ReedTheme.typography.body1Regular,
        )
    }
}

@ComponentPreview
@Composable
private fun SentenceBoxPreview() {
    ReedTheme {
        SentenceBox(
            onClick = {},
            sentence = "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
        )
    }
}
