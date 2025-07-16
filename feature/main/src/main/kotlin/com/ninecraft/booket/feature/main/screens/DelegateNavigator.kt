package com.ninecraft.booket.feature.main.screens

import com.ninecraft.booket.feature.main.component.MainTab
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

class DelegateNavigator(
    private val childNavigator: Navigator,
    private val rootNavigator: Navigator,
) : Navigator {

    private val bottomNavigationScreenClasses = MainTab.entries.map { it.screen::class }.toSet()
    
    private fun Screen.isBottomNavigationScreen(): Boolean = 
        this::class in bottomNavigationScreenClasses

    override fun goTo(screen: Screen): Boolean {
        return if (screen.isBottomNavigationScreen()) {
            childNavigator.goTo(screen)
        } else {
            rootNavigator.goTo(screen)
        }
    }

    override fun pop(result: PopResult?): Screen? {
        val currentScreen = childNavigator.peek()
        return if (currentScreen?.isBottomNavigationScreen() == true) {
            childNavigator.pop(result)
        } else {
            rootNavigator.pop(result)
        }
    }

    override fun peek(): Screen? {
        val childScreen = childNavigator.peek()
        return if (childScreen?.isBottomNavigationScreen() == true) {
            childScreen
        } else {
            rootNavigator.peek()
        }
    }

    override fun resetRoot(
        newRoot: Screen,
        saveState: Boolean,
        restoreState: Boolean,
    ): ImmutableList<Screen> {
        return if (newRoot.isBottomNavigationScreen()) {
            childNavigator.resetRoot(newRoot, saveState, restoreState)
        } else {
            rootNavigator.resetRoot(newRoot, saveState, restoreState)
        }
    }

    override fun peekBackStack(): ImmutableList<Screen> {
        val childScreen = childNavigator.peek()
        return if (childScreen?.isBottomNavigationScreen() == true) {
            childNavigator.peekBackStack()
        } else {
            rootNavigator.peekBackStack()
        }
    }
}