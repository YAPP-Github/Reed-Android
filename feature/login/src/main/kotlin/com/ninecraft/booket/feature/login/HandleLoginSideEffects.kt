package com.ninecraft.booket.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun HandleLoginEffects(
    state: LoginScreen.State,
    eventSink: (LoginScreen.Event) -> Unit,
) {
    val context = LocalContext.current
    val kakaoAuthClient = remember { KakaoAuthClient() }

    LaunchedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is LoginScreen.SideEffect.KakaoLogin -> {
                kakaoAuthClient.loginWithKakao(
                    context = context,
                    onSuccess = { token ->
                        eventSink(LoginScreen.Event.LoginSuccess(token))
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
    }

    if (state.sideEffect != null) {
        eventSink(LoginScreen.Event.InitSideEffect)
    }
}
