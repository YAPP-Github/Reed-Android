package com.ninecraft.booket.feature.main.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.core.designsystem.component.ReedScaffold
import com.ninecraft.booket.feature.main.component.MainBottomBar
import com.ninecraft.booket.feature.main.component.MainTab
import com.ninecraft.booket.screens.BottomNavigationScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.toImmutableList

@CircuitInject(BottomNavigationScreen::class, ActivityRetainedComponent::class)
@Composable
fun BottomNavigation(
    state: BottomNavigationUiState,
    modifier: Modifier = Modifier,
) {
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainBottomBar(
                tabs = MainTab.entries.toImmutableList(),
                currentTab = state.currentTab,
                onTabSelected = { tab ->
                    state.eventSink(BottomNavigationUiEvent.OnTabSelected(tab))
                },
            )
        },
    ) { innerPadding ->
        ContentWithOverlays {
            NavigableCircuitContent(
                navigator = state.navigator,
                backStack = state.childBackStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}
