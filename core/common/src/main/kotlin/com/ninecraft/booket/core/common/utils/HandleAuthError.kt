package com.ninecraft.booket.core.common.utils

import retrofit2.HttpException

fun handleAuthError(
    exception: Throwable,
    onGeneralError: (String) -> Unit,
    onLoginRequired: () -> Unit,
) {
    when {
        exception is HttpException && exception.code() == 401 -> {
            onLoginRequired()
        }

        else -> {
            exception.message?.let { onGeneralError(it) }
        }
    }
}
