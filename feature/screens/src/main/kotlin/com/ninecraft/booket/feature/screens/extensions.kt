package com.ninecraft.booket.feature.screens

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
