package com.ninecraft.booket.feature.screens.extensions

import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.ReedScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.delay

suspend fun Navigator.delayedGoTo(screen: ReedScreen, delayMillis: Long = 200L) {
    delay(delayMillis)
    goTo(screen)
}

suspend fun Navigator.delayedPop(delayMillis: Long = 200L) {
    delay(delayMillis)
    pop()
}

fun Navigator.popUntilOrGoTo(screen: Screen) {
    if (screen in peekBackStack()) {
        popUntil { it == screen }
    } else {
        goTo(screen)
    }
}

suspend fun Navigator.redirectToLogin(): Screen? {
    val currentScreen = peek()
    delayedGoTo(LoginScreen(currentScreen))
    return currentScreen
}

