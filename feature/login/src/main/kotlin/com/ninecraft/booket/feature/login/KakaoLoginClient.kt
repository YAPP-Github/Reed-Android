package com.ninecraft.booket.feature.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.user.UserApiClient
import com.ninecraft.booket.core.designsystem.R as designR
import javax.inject.Inject
import com.orhanobut.logger.Logger

internal class KakaoLoginClient @Inject constructor() {
    fun loginWithKakao(
        context: Context,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> handleLoginError(context, error, onFailure)
                token != null -> handleLoginSuccess(token, onSuccess, onFailure, context)
                else -> onFailure(context.getString(designR.string.unknown_error_message))
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }

    private fun handleLoginError(
        context: Context,
        error: Throwable,
        onFailure: (String) -> Unit,
    ) {
        when {
            (error is AuthError && error.response.error == "ProtocolError") -> {
                Logger.e("로그인 실패: ${error.response.error}, ${error.response.errorDescription}")
                onFailure(context.getString(designR.string.network_error_message))
            }

            else -> {
                Logger.e("로그인 실패: ${error.message}")
                onFailure(context.getString(designR.string.unknown_error_message))
            }
        }
    }

    private fun handleLoginSuccess(
        token: OAuthToken,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
        context: Context,
    ) {
        UserApiClient.instance.me { user, _ ->
            user?.let {
                onSuccess(token.accessToken)
            } ?: onFailure(context.getString(designR.string.unknown_error_message))
        }
    }
}
