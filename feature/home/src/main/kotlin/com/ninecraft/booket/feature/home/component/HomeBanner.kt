package com.ninecraft.booket.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.HomeBg
import com.ninecraft.booket.core.designsystem.theme.ReedTheme

@Composable
fun HomeBanner(
    onBookRegisterClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(HomeBg)
            .padding(
                top = ReedTheme.spacing.spacing4,
                start = ReedTheme.spacing.spacing6,
                end = ReedTheme.spacing.spacing5,
            ),
    ) {
        Column {
            Text(
                text = "문장 기록하고\n씨앗을 모아볼까요?",
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.heading1Bold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Row(
                modifier = Modifier.clickableSingle {
                    onBookRegisterClick()
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "책 등록하기",
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.body2Medium,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
                    contentDescription = "Chevron Right Icon",
                    tint = ReedTheme.colors.contentBrand,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun HomeBannerPreview() {
    ReedTheme {
        HomeBanner(
            onBookRegisterClick = {},
        )
    }
}
