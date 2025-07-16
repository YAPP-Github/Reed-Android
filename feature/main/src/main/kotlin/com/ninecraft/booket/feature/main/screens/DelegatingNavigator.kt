package com.ninecraft.booket.feature.main.screens

import com.ninecraft.booket.feature.main.component.MainTab
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

class DelegatingNavigator(
    private val childNavigator: Navigator,
    private val rootNavigator: Navigator,
) : Navigator {

    override fun goTo(screen: Screen): Boolean {
        return if (MainTab.entries.any { it.screen::class == screen::class }) {
            childNavigator.goTo(screen)
        } else {
            rootNavigator.goTo(screen)
        }
    }

    override fun pop(result: PopResult?): Screen? {
        val currentScreen = childNavigator.peek()
        return if (currentScreen != null && MainTab.entries.any { it.screen::class == currentScreen::class }) {
            childNavigator.pop(result)
        } else {
            rootNavigator.pop(result)
        }
    }

    override fun peek(): Screen? {
        val childScreen = childNavigator.peek()
        return if (childScreen != null && MainTab.entries.any { it.screen::class == childScreen::class }) {
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
        return if (MainTab.entries.any { it.screen::class == newRoot::class }) {
            childNavigator.resetRoot(newRoot, saveState, restoreState)
        } else {
            rootNavigator.resetRoot(newRoot, saveState, restoreState)
        }
    }

    override fun peekBackStack(): ImmutableList<Screen> {
        val childScreen = childNavigator.peek()
        return if (childScreen != null && MainTab.entries.any { it.screen::class == childScreen::class }) {
            childNavigator.peekBackStack()
        } else {
            rootNavigator.peekBackStack()
        }
    }
}
