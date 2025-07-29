package com.ninecraft.booket.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.HomeBg
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.home.component.BookCard
import com.ninecraft.booket.feature.home.component.HomeBanner
import com.ninecraft.booket.feature.home.component.HomeHeader
import com.ninecraft.booket.feature.screens.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun HomeUi(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HomeContent(
            state = state,
            modifier = modifier,
        )
    }
}

@Composable
internal fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    val dummyBooks = listOf(
        Book("여름은 오래 그곳에 남아", "마쓰이에 마사시", "비채", "https://image.aladin.co.kr/product/7492/9/cover200/8934972203_1.jpg", 3),
        Book("여름은 오래 그곳에 남아", "마쓰이에 마사시", "비채", "https://image.aladin.co.kr/product/7492/9/cover200/8934972203_1.jpg", 3),
        Book("여름은 오래 그곳에 남아", "마쓰이에 마사시", "비채", "https://image.aladin.co.kr/product/7492/9/cover200/8934972203_1.jpg", 3)
    )

    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = HomeBg,
            darkIcons = true,
        )
        onDispose {
            systemUiController.setStatusBarColor(
                color = White,
                darkIcons = true,
            )
        }
    }

    HomeHeader(
        onSettingsClick = {
            state.eventSink(HomeUiEvent.OnSettingsClick)
        },
        modifier = modifier,
    )
    HomeBanner(
        onBookRegisterClick = {
            state.eventSink(HomeUiEvent.OnBookRegisterClick)
        },
        modifier = modifier,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ReedTheme.colors.baseSecondary),
    ) {
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
        Text(
            text = stringResource(R.string.home_content_label_reading_now),
            modifier = Modifier.padding(start = ReedTheme.spacing.spacing5),
            color = ReedTheme.colors.contentSecondary,
            style = ReedTheme.typography.headline2Medium,
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
        val pagerState = rememberPagerState(pageCount = { dummyBooks.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = ReedTheme.spacing.spacing5),
            pageSpacing = ReedTheme.spacing.spacing5,
        ) { page ->
            BookCard(
                bookInfo = dummyBooks[page],
                onBookDetailClick = {
                    state.eventSink(HomeUiEvent.OnBookDetailClick)
                },
                onRecordButtonClick = {
                    state.eventSink(HomeUiEvent.OnRecordButtonClick)
                }
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) ReedTheme.colors.bgPrimary else ReedTheme.colors.bgSecondaryPressed
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomePreview() {
    ReedTheme {
        HomeUi(
            state = HomeUiState(
                eventSink = {},
            ),
        )
    }
}
