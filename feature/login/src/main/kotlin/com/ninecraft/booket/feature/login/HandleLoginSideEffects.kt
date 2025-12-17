package com.ninecraft.booket.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect
import kotlinx.coroutines.launch

@Composable
internal fun HandleLoginSideEffects(
    state: LoginUiState,
    eventSink: (LoginUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val kakaoLoginClient = remember { KakaoLoginClient() }
    val googleLoginClient = remember { GoogleLoginClient() }

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is LoginSideEffect.KakaoLogin -> {
                kakaoLoginClient.loginWithKakao(
                    context = context,
                    onSuccess = { token ->
                        eventSink(
                            LoginUiEvent.Login(
                                providerType = LoginUiEvent.PROVIDER_TYPE_KAKAO,
                                token = token,
                            ),
                        )
                    },
                    onFailure = { errorMessage ->
                        eventSink(LoginUiEvent.LoginFailure(errorMessage))
                    },
                )
            }

            is LoginSideEffect.GoogleLogin -> {
                scope.launch {
                    googleLoginClient.loginWithGoogle(
                        context = context,
                        webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID,
                        onSuccess = { idToken ->
                            eventSink(
                                LoginUiEvent.Login(
                                    providerType = LoginUiEvent.PROVIDER_TYPE_GOOGLE,
                                    token = idToken,
                                ),
                            )
                        },
                        onFailure = { errorMessage ->
                            eventSink(LoginUiEvent.LoginFailure(errorMessage))
                        },
                    )
                }
            }

            is LoginSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
