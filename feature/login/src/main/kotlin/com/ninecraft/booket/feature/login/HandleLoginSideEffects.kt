package com.ninecraft.booket.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun HandleLoginSideEffects(
    state: LoginScreen.State,
    eventSink: (LoginScreen.Event) -> Unit,
) {
    val context = LocalContext.current
    val kakaoLoginClient = remember { KakaoLoginClient() }

    LaunchedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is LoginScreen.SideEffect.KakaoLogin -> {
                kakaoLoginClient.loginWithKakao(
                    context = context,
                    onSuccess = { token ->
                        eventSink(LoginScreen.Event.Login(token))
                    },
                    onFailure = { errorMessage ->
                        eventSink(LoginScreen.Event.LoginFailure(errorMessage))
                    },
                )
            }

            is LoginScreen.SideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(LoginScreen.Event.InitSideEffect)
        }
    }
}
