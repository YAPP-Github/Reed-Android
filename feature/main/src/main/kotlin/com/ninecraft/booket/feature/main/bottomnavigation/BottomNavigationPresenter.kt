package com.ninecraft.booket.feature.main.bottomnavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ninecraft.booket.feature.main.component.getCurrentTab
import com.ninecraft.booket.feature.screens.BottomNavigationScreen
import com.ninecraft.booket.feature.screens.HomeScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class BottomNavigationPresenter @AssistedInject constructor(
    @Assisted private val rootNavigator: Navigator,
) : Presenter<BottomNavigationUiState> {

    @Composable
    override fun present(): BottomNavigationUiState {
        val childBackStack = rememberSaveableBackStack(root = HomeScreen)
        val childNavigator = rememberCircuitNavigator(childBackStack)
        val delegateNavigator = remember(childNavigator, rootNavigator) {
            DelegateNavigator(childNavigator, rootNavigator)
        }
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
            backStack = childBackStack,
            navigator = delegateNavigator,
            currentTab = currentTab,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(BottomNavigationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(rootNavigator: Navigator): BottomNavigationPresenter
    }
}
