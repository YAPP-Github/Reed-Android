package com.ninecraft.booket.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleLoginSideEffects(
    state: LoginUiState,
    eventSink: (LoginUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val kakaoLoginClient = remember { KakaoLoginClient() }

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is LoginSideEffect.KakaoLogin -> {
                kakaoLoginClient.loginWithKakao(
                    context = context,
                    onSuccess = { token ->
                        eventSink(LoginUiEvent.Login(token))
                    },
                    onFailure = { errorMessage ->
                        eventSink(LoginUiEvent.LoginFailure(errorMessage))
                    },
                )
            }

            is LoginSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
