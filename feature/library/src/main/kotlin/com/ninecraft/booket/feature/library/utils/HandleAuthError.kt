package com.ninecraft.booket.feature.library.utils

import com.ninecraft.booket.feature.login.LoginScreen
import com.slack.circuit.runtime.Navigator

fun handleAuthError(exception: Throwable, navigator: Navigator, onError: (String) -> Unit) {
    if (exception.message?.contains("401") == true || exception.message?.contains("Unauthorized") == true) {
        navigator.resetRoot(LoginScreen)
    } else {
        exception.message?.let { onError(it) }
    }
}
