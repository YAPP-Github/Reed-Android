package com.ninecraft.booket.core.common.utils

import com.ninecraft.booket.core.common.constants.ErrorDialogSpec
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.event.ErrorEvent
import com.ninecraft.booket.core.common.event.ErrorEventHelper
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

fun postErrorDialog(
    errorScope: ErrorScope,
    exception: Throwable,
    action: () -> Unit = {},
) {
    val spec = buildDialog(
        scope = errorScope,
        exception = exception,
        action = action,
    )

    ErrorEventHelper.sendError(event = ErrorEvent.ShowDialog(spec))
}

private fun buildDialog(
    scope: ErrorScope,
    exception: Throwable,
    action: () -> Unit,
): ErrorDialogSpec {
    val message = when {
        exception.isNetworkError() -> {
            "네트워크 연결이 불안정합니다.\n인터넷 연결을 확인해주세요"
        }
        exception is HttpException -> {
            when (scope) {
                ErrorScope.GLOBAL -> {
                    "알 수 없는 문제가 발생했어요.\n다시 시도해주세요"
                }

                ErrorScope.LOGIN -> {
                    "예기치 않은 오류가 발생했습니다.\n다시 로그인 해주세요."
                }

                ErrorScope.BOOK_REGISTER -> {
                    "도서 등록 중 오류가 발생했어요.\n다시 시도해주세요"
                }

                ErrorScope.RECORD_REGISTER -> {
                    "기록 저장에 실패했어요.\n다시 시도해주세요"
                }
            }
        }
        else -> {
            "알 수 없는 문제가 발생했어요.\n다시 시도해주세요"
        }
    }

    return ErrorDialogSpec(message = message, buttonLabel ="확인" , action = action)
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

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectException ||
        this is SocketTimeoutException ||
        this is IOException
}
