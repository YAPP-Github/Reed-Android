package com.ninecraft.booket.feature.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.ResourceImage
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.Black
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.onboarding.component.PagerIndicator

private const val ONBOARDING_STEPS_COUNT = 3

@Composable
internal fun OnBoardingUi(
    setOnboardingCompletedStatus: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { ONBOARDING_STEPS_COUNT })

    ReedFullScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(98.dp))
                                ResourceImage(
                                    imageRes = R.drawable.img_onboarding_second_graphic,
                                    contentDescription = "Onboarding First Graphic",
                                )
                                Text(
                                    text = stringResource(R.string.onboarding_first_step_title),
                                    color = Black,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                                Text(
                                    text = stringResource(R.string.onboarding_first_step_description),
                                    color = ReedTheme.colors.contentSecondary,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(98.dp))
                            }
                            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                                Spacer(modifier = Modifier.height(16.dp))
                                PagerIndicator(
                                    pageCount = ONBOARDING_STEPS_COUNT,
                                    pagerState = pagerState,
                                )
                                ReedButton(
                                    onClick = { setOnboardingCompletedStatus(true) },
                                    text = stringResource(R.string.next),
                                    sizeStyle = largeButtonStyle,
                                    colorStyle = ReedButtonColorStyle.PRIMARY,
                                )
                            }
                        }
                    }

                    1 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(98.dp))
                                ResourceImage(
                                    imageRes = R.drawable.img_onboarding_second_graphic,
                                    contentDescription = "Onboarding First Graphic",
                                )
                                Text(
                                    text = stringResource(R.string.onboarding_second_step_title),
                                    color = Black,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                                Text(
                                    text = stringResource(R.string.onboarding_second_step_description),
                                    color = ReedTheme.colors.contentSecondary,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(98.dp))
                            }
                            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                                Spacer(modifier = Modifier.height(16.dp))
                                PagerIndicator(
                                    pageCount = ONBOARDING_STEPS_COUNT,
                                    pagerState = pagerState,
                                )
                                ReedButton(
                                    onClick = { setOnboardingCompletedStatus(true) },
                                    text = stringResource(R.string.next),
                                    sizeStyle = largeButtonStyle,
                                    colorStyle = ReedButtonColorStyle.PRIMARY,
                                )
                            }
                        }
                    }

                    2 -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(98.dp))
                                ResourceImage(
                                    imageRes = R.drawable.img_onboarding_second_graphic,
                                    contentDescription = "Onboarding First Graphic",
                                )
                                Text(
                                    text = stringResource(R.string.onboarding_third_step_title),
                                    color = Black,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                                Text(
                                    text = stringResource(R.string.onboarding_third_step_description),
                                    color = ReedTheme.colors.contentSecondary,
                                    style = ReedTheme.typography.heading1Bold,
                                )
                                Spacer(modifier = Modifier.height(98.dp))
                            }
                            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                                Spacer(modifier = Modifier.height(16.dp))
                                PagerIndicator(
                                    pageCount = ONBOARDING_STEPS_COUNT,
                                    pagerState = pagerState,
                                )
                                ReedButton(
                                    onClick = { setOnboardingCompletedStatus(true) },
                                    text = stringResource(R.string.next),
                                    sizeStyle = largeButtonStyle,
                                    colorStyle = ReedButtonColorStyle.PRIMARY,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun OnBoardingScreenPreview() {
    ReedTheme {
        OnBoardingUi(
            setOnboardingCompletedStatus = {},
        )
    }
}
