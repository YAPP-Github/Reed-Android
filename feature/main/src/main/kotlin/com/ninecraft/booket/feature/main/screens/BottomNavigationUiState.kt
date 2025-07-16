package com.ninecraft.booket.feature.main.screens

import com.ninecraft.booket.feature.main.component.MainTab
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator

data class BottomNavigationUiState(
    val childBackStack: SaveableBackStack,
    val childNavigator: Navigator,
    val currentTab: MainTab?,
    val eventSink: (BottomNavigationUiEvent) -> Unit,
) : CircuitUiState

sealed interface BottomNavigationUiEvent {
    data class OnTabSelected(val tab: MainTab) : BottomNavigationUiEvent
}
