package com.ninecraft.booket.feature.main.screens

import androidx.compose.runtime.Composable
import com.ninecraft.booket.feature.main.component.MainTab
import com.ninecraft.booket.screens.BottomNavigationScreen
import com.ninecraft.booket.screens.HomeScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class BottomNavigationPresenter @AssistedInject constructor() : Presenter<BottomNavigationUiState> {

    @Composable
    override fun present(): BottomNavigationUiState {
        val childBackStack = rememberSaveableBackStack(root = HomeScreen)
        val childNavigator = rememberCircuitNavigator(childBackStack)

        val currentTab = getCurrentTab(childBackStack)

        fun handleEvent(event: BottomNavigationUiEvent) {
            when (event) {
                is BottomNavigationUiEvent.OnTabSelected -> {
                    childNavigator.resetRoot(
                        newRoot = event.tab.screen,
                        saveState = true,
                        restoreState = true,
                    )
                }
            }
        }

        return BottomNavigationUiState(
            childBackStack = childBackStack,
            childNavigator = childNavigator,
            currentTab = currentTab,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(BottomNavigationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(): BottomNavigationPresenter
    }
}

@Composable
private fun getCurrentTab(backStack: SaveableBackStack): MainTab? {
    val currentScreen = backStack.topRecord?.screen
    return currentScreen?.let { screen ->
        MainTab.entries.find { it.screen::class == currentScreen::class }
    }
}
