package com.ninecraft.booket.core.common.utils

import com.ninecraft.booket.core.network.response.ErrorResponse
import com.orhanobut.logger.Logger
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun handleException(
    exception: Throwable,
    onError: (String) -> Unit,
    onLoginRequired: () -> Unit,
) {
    when {
        exception is HttpException && exception.code() == 401 -> {
            onLoginRequired()
        }

        exception is HttpException -> {
            val serverMessage = exception.parseErrorMessage()
            val message = serverMessage ?: getHttpErrorMessage(exception.code())
            Logger.e("HTTP ${exception.code()}: $message")
            onError(message)
        }

        exception.isNetworkError() -> {
            onError("네트워크 연결을 확인해주세요.")
        }

        else -> {
            val errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다"
            Logger.e(errorMessage)
            onError(errorMessage)
        }
    }
}

@Suppress("TooGenericExceptionCaught")
private fun HttpException.parseErrorMessage(): String? {
    return try {
        val errorBody = response()?.errorBody()?.string()
        if (errorBody.isNullOrBlank()) return null
        Json.decodeFromString<ErrorResponse>(errorBody).getErrorMessage()
    } catch (e: SerializationException) {
        Logger.e("JSON parsing failed: ${e.message}")
        null
    } catch (e: IOException) {
        Logger.e("Failed to read error body: ${e.message}")
        null
    } catch (e: Exception) {
        Logger.e("Unexpected error parsing response: ${e.message}")
        null
    }
}

private fun getHttpErrorMessage(statusCode: Int): String {
    return when (statusCode) {
        400 -> "요청이 올바르지 않습니다"
        403 -> "접근 권한이 없습니다"
        404 -> "존재하지 않는 데이터입니다"
        429 -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요"
        in 400..499 -> "요청 처리 중 오류가 발생했습니다"
        in 500..599 -> "서버 오류가 발생했습니다"
        else -> "알 수 없는 오류가 발생했습니다"
    }
}

private fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectException ||
        this is SocketTimeoutException ||
        this is IOException
}
