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

    override fun pop(result: PopResult?): Screen? = childNavigator.pop(result)

    override fun peek(): Screen? = childNavigator.peek()

    override fun resetRoot(
        newRoot: Screen,
        saveState: Boolean,
        restoreState: Boolean,
    ): ImmutableList<Screen> = childNavigator.resetRoot(newRoot, saveState, restoreState)

    override fun peekBackStack(): ImmutableList<Screen> = childNavigator.peekBackStack()
}
