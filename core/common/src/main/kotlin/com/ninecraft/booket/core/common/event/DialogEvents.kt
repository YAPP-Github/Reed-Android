package com.ninecraft.booket.core.common.event

import com.ninecraft.booket.core.common.constants.DialogSpec
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.utils.isNetworkError
import retrofit2.HttpException

fun postErrorDialog(
    errorScope: ErrorScope,
    exception: Throwable,
    confirmLabel: String = "확인",
    onConfirm: () -> Unit = {},
) {
    val (title, message) = when {
        exception.isNetworkError() -> {
            null to "네트워크 연결이 불안정합니다.\n인터넷 연결을 확인해주세요"
        }

        exception is HttpException -> {
            when (errorScope) {
                ErrorScope.GLOBAL -> {
                    null to "알 수 없는 문제가 발생했어요.\n다시 시도해주세요"
                }

                ErrorScope.LOGIN -> {
                    "로그인 오류" to "예기치 않은 오류가 발생했습니다.\n다시 로그인 해주세요."
                }

                ErrorScope.AUTH_SESSION_EXPIRED -> {
                    null to "세션이 만료되었어요.\n다시 로그인 해주세요"
                }
            }
        }

        else -> {
            null to "알 수 없는 문제가 발생했어요.\n다시 시도해주세요"
        }
    }

    val spec = DialogSpec(
        title = title,
        message = message,
        confirmLabel = confirmLabel,
        onConfirm = onConfirm,
    )

    EventHelper.sendEvent(event = ReedEvent.ShowDialog(spec))
}
