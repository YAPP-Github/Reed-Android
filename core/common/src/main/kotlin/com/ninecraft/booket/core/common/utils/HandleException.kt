package com.ninecraft.booket.core.common.utils

import com.orhanobut.logger.Logger
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ErrorHandlerActions {
    fun setServerErrorDialogVisible(flag: Boolean)
    fun setNetworkErrorDialogVisible(flag: Boolean)
}

fun handleException(exception: Throwable, actions: ErrorHandlerActions) {
    when (exception) {
        is HttpException -> {
            if (exception.code() in 500..599) {
                actions.setServerErrorDialogVisible(true)
            } else {
                exception.message?.let { Logger.e(it) }
            }
        }

        is UnknownHostException, is ConnectException -> {
            actions.setNetworkErrorDialogVisible(true)
        }

        is SocketTimeoutException -> {
            actions.setServerErrorDialogVisible(true)
        }

        else -> {
            exception.message?.let { Logger.e(it) }
        }
    }
}
