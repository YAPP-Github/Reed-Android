package com.ninecraft.booket.feature.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.HighlightedText
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.Black
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.feature.onboarding.R

@Composable
internal fun OnboardingPage(
    imageRes: Int,
    titleRes: Int,
    highlightTextRes: Int,
    descriptionRes: Int,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Onboarding Second Graphic",
            modifier = Modifier.height(274.dp)
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing8))
        Text(
            text = HighlightedText(
                fullText = stringResource(titleRes),
                highlightText = stringResource(highlightTextRes),
                highlightColor = ReedTheme.colors.bgPrimary,
            ),
            color = Black,
            textAlign = TextAlign.Center,
            style = ReedTheme.typography.heading1Bold,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        Text(
            text = stringResource(descriptionRes),
            color = ReedTheme.colors.contentTertiary,
            textAlign = TextAlign.Center,
            style = ReedTheme.typography.body2Medium,
        )
        Spacer(modifier = Modifier.weight(1f, fill = false))
    }
}

@ComponentPreview
@Composable
private fun OnboardingPagePreview() {
    ReedTheme {
        OnboardingPage(
            imageRes = R.drawable.img_onboarding_first,
            titleRes = R.string.onboarding_first_page_title,
            highlightTextRes = R.string.onboarding_first_highlight_text,
            descriptionRes = R.string.onboarding_first_page_description,
        )
    }
}
