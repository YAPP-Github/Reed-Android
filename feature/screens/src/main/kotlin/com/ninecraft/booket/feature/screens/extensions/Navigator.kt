package com.ninecraft.booket.feature.screens.extensions

import com.ninecraft.booket.feature.screens.ReedScreen
import com.slack.circuit.runtime.Navigator
import kotlinx.coroutines.delay

suspend fun Navigator.delayedGoTo(screen: ReedScreen, delayMillis: Long = 200L) {
    delay(delayMillis)
    goTo(screen)
}

suspend fun Navigator.delayedPop(delayMillis: Long = 200L) {
    delay(delayMillis)
    pop()
}
