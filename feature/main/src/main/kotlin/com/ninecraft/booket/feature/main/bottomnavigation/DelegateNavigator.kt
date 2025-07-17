package com.ninecraft.booket.feature.main.bottomnavigation

import com.ninecraft.booket.feature.main.component.MainTab
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

class DelegateNavigator(
    private val childNavigator: Navigator,
    private val rootNavigator: Navigator,
) : Navigator {
    private fun isMainTabScreen(screen: Screen): Boolean {
        return MainTab.entries.any { it.screen::class == screen::class }
    }

    override fun goTo(screen: Screen): Boolean {
        return if (isMainTabScreen(screen)) {
            childNavigator.goTo(screen)
        } else {
            rootNavigator.goTo(screen)
        }
    }

    override fun pop(result: PopResult?): Screen? {
        return childNavigator.pop(result)
    }

    override fun peek(): Screen? {
        return childNavigator.peek()
    }

    override fun resetRoot(
        newRoot: Screen,
        saveState: Boolean,
        restoreState: Boolean,
    ): ImmutableList<Screen> {
        return if (isMainTabScreen(newRoot)) {
            childNavigator.resetRoot(newRoot, saveState, restoreState)
        } else {
            rootNavigator.resetRoot(newRoot, saveState, restoreState)
        }
    }

    override fun peekBackStack(): ImmutableList<Screen> {
        return childNavigator.peekBackStack()
    }
}
