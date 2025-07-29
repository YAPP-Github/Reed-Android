package com.ninecraft.booket.feature.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.utils.HighlightedText
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.ResourceImage
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.Black
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.onboarding.component.PagerIndicator
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun OnboardingUi(
    state: OnboardingUiState,
    modifier: Modifier = Modifier,
) {
    ReedFullScreen(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = state.pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                when (page) {
                    0 -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            ResourceImage(
                                imageRes = R.drawable.img_onboarding_second_graphic,
                                contentDescription = "Onboarding First Graphic",
                            )
                            Text(
                                text = HighlightedText(
                                    fullText = stringResource(R.string.onboarding_first_step_title),
                                    highlightText = stringResource(R.string.onboarding_first_highlight_text),
                                    highlightColor = ReedTheme.colors.bgPrimary,
                                ),
                                color = Black,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.heading1Bold,
                            )
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                            Text(
                                text = stringResource(R.string.onboarding_first_step_description),
                                color = ReedTheme.colors.contentTertiary,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.body2Medium,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    1 -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            ResourceImage(
                                imageRes = R.drawable.img_onboarding_second_graphic,
                                contentDescription = "Onboarding Second Graphic",
                            )
                            Text(
                                text = HighlightedText(
                                    fullText = stringResource(R.string.onboarding_second_step_title),
                                    highlightText = stringResource(R.string.onboarding_second_highlight_text),
                                    highlightColor = ReedTheme.colors.bgPrimary,
                                ),
                                color = Black,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.heading1Bold,
                            )
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                            Text(
                                text = stringResource(R.string.onboarding_second_step_description),
                                color = ReedTheme.colors.contentTertiary,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.body2Medium,
                            )
                            Spacer(modifier = Modifier.weight(1f, fill = false))
                        }
                    }

                    2 -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            ResourceImage(
                                imageRes = R.drawable.img_onboarding_second_graphic,
                                contentDescription = "Onboarding Third Graphic",
                            )
                            Text(
                                text = HighlightedText(
                                    fullText = stringResource(R.string.onboarding_third_step_title),
                                    highlightText = stringResource(R.string.onboarding_third_highlight_text),
                                    highlightColor = ReedTheme.colors.bgPrimary,
                                ),
                                color = Black,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.heading1Bold,
                            )
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                            Text(
                                text = stringResource(R.string.onboarding_third_step_description),
                                color = ReedTheme.colors.contentTertiary,
                                textAlign = TextAlign.Center,
                                style = ReedTheme.typography.body2Medium,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            PagerIndicator(
                pageCount = ONBOARDING_STEPS_COUNT,
                pagerState = state.pagerState,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            ReedButton(
                onClick = {
                    state.eventSink(OnboardingUiEvent.OnNextButtonClick(state.pagerState.currentPage))
                },
                text = stringResource(R.string.next),
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(
                        horizontal = ReedTheme.spacing.spacing5,
                    ),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        }
    }
}

@DevicePreview
@Composable
private fun OnboardingScreenPreview() {
    ReedTheme {
        OnboardingUi(
            state = OnboardingUiState(
                pagerState = rememberPagerState(pageCount = { ONBOARDING_STEPS_COUNT }),
                eventSink = {},
            ),
        )
    }
}
