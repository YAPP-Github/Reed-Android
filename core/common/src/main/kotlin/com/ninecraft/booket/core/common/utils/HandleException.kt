package com.ninecraft.booket.core.common.utils

import com.orhanobut.logger.Logger
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun handleException(
    exception: Throwable,
    onLoginRequired: () -> Unit,
    onServerError: (String) -> Unit,
    onNetworkError: (String) -> Unit,
) {
    when {
        exception is HttpException && exception.code() == 401 -> {
            onLoginRequired()
        }

        exception is HttpException && exception.code() in 500..599 -> {
            onServerError("서버 오류가 발생했습니다.")
        }

        exception is UnknownHostException || exception is ConnectException -> {
            onNetworkError("네트워크 연결을 확인해주세요.")
        }

        exception is SocketTimeoutException -> {
            onServerError("서버 응답 시간이 초과되었습니다.")
        }

        else -> {
            Logger.e(exception.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}
