package com.ninecraft.booket.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.HomeBg
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.home.R
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
fun HomeBanner(
    onBookRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.home_seed))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(HomeBg)
            .padding(end = ReedTheme.spacing.spacing5),
    ) {
        Column(
            modifier = Modifier.padding(
                top = ReedTheme.spacing.spacing4,
                start = ReedTheme.spacing.spacing6,
                end = ReedTheme.spacing.spacing5,
            ),
        ) {
            Text(
                text = stringResource(R.string.home_banner_title),
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
                    text = stringResource(R.string.home_banner_book_register),
                    color = ReedTheme.colors.contentBrand,
                    style = ReedTheme.typography.body2Medium,
                )
                Spacer(modifier = Modifier.size(ReedTheme.spacing.spacing1))
                Icon(
                    imageVector = ImageVector.vectorResource(id = designR.drawable.ic_chevron_right),
                    contentDescription = "Chevron Right Icon",
                    modifier = Modifier.size(ReedTheme.spacing.spacing5),
                    tint = ReedTheme.colors.contentBrand,
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(ReedTheme.colors.baseSecondary)
                .align(Alignment.BottomCenter),
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(144.dp)
                .align(Alignment.BottomEnd),
        )
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
