package com.ninecraft.booket.feature.home

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ninecraft.booket.core.common.extensions.toErrorType
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.HomeBg
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedErrorUi
import com.ninecraft.booket.core.ui.component.ReedLoadingIndicator
import com.ninecraft.booket.feature.home.component.BookCard
import com.ninecraft.booket.feature.home.component.EmptyBookCard
import com.ninecraft.booket.feature.home.component.HomeBanner
import com.ninecraft.booket.feature.home.component.HomeHeader
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.component.MainBottomBar
import com.ninecraft.booket.feature.screens.component.MainTab
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun HomeUi(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    HandleHomeSideEffects(state = state)

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        state.eventSink(HomeUiEvent.OnNotificationPermissionResult(granted))
    }

    if (!state.isGuestMode) {
        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permission = android.Manifest.permission.POST_NOTIFICATIONS
                val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

                if (!isGranted) {
                    permissionLauncher.launch(permission)
                }
            } else {
                state.eventSink(HomeUiEvent.OnNotificationPermissionResult(true))
            }
        }
    }

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                tabs = MainTab.entries.toImmutableList(),
                currentTab = MainTab.HOME,
                onTabSelected = { tab ->
                    state.eventSink(HomeUiEvent.OnTabSelected(tab))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HomeBg)
                .padding(innerPadding),
        ) {
            HomeHeader(
                onSettingsClick = {
                    state.eventSink(HomeUiEvent.OnSettingsClick)
                },
            )
            HomeBanner(
                onBookRegisterClick = {
                    state.eventSink(HomeUiEvent.OnBookRegisterClick)
                },
            )
            HomeContent(
                state = state,
                modifier = Modifier,
            )
        }
    }
}

@Composable
internal fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ReedTheme.colors.baseSecondary),
    ) {
        if (state.isGuestMode) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
                Text(
                    text = stringResource(R.string.home_content_label_reading_now),
                    modifier = Modifier.padding(start = ReedTheme.spacing.spacing5),
                    color = ReedTheme.colors.contentSecondary,
                    style = ReedTheme.typography.headline2Medium,
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
                EmptyBookCard(
                    onBookRegisterClick = {
                        state.eventSink(HomeUiEvent.OnBookRegisterClick)
                    },
                    modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
                )
                Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
            }
        } else {
            when (state.uiState) {
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    ReedLoadingIndicator()
                }

                is UiState.Success -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
                        Text(
                            text = stringResource(R.string.home_content_label_reading_now),
                            modifier = Modifier.padding(start = ReedTheme.spacing.spacing5),
                            color = ReedTheme.colors.contentSecondary,
                            style = ReedTheme.typography.headline2Medium,
                        )
                        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))

                        if (state.recentBooks.isEmpty()) {
                            EmptyBookCard(
                                onBookRegisterClick = {
                                    state.eventSink(HomeUiEvent.OnBookRegisterClick)
                                },
                                modifier = Modifier.padding(horizontal = ReedTheme.spacing.spacing5),
                            )
                        } else {
                            val pagerState = rememberPagerState(pageCount = { state.recentBooks.size })

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = ReedTheme.spacing.spacing5),
                                pageSpacing = ReedTheme.spacing.spacing5,
                            ) { page ->
                                BookCard(
                                    recentBookInfo = state.recentBooks[page],
                                    onBookDetailClick = {
                                        state.eventSink(
                                            HomeUiEvent.OnBookDetailClick(
                                                state.recentBooks[page].userBookId,
                                                state.recentBooks[page].isbn13,
                                            ),
                                        )
                                    },
                                    onRecordButtonClick = {
                                        state.eventSink(HomeUiEvent.OnRecordButtonClick(state.recentBooks[page].userBookId))
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                repeat(pagerState.pageCount) { iteration ->
                                    val color =
                                        if (pagerState.currentPage == iteration) ReedTheme.colors.bgPrimary else ReedTheme.colors.bgSecondaryPressed
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                            .background(color),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing7))
                        }
                        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing6))
                    }
                }

                is UiState.Error -> {
                    ReedErrorUi(
                        errorType = state.uiState.exception.toErrorType(),
                        onRetryClick = { state.eventSink(HomeUiEvent.OnRetryClick) },
                    )
                }
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
                uiState = UiState.Success,
                eventSink = {},
            ),
        )
    }
}
