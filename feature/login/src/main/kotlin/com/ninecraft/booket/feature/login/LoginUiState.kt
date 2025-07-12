package com.ninecraft.booket.feature.login

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

data class LoginUiState(
    val isLoading: Boolean = false,
    val sideEffect: LoginSideEffect? = null,
    val eventSink: (LoginUiEvent) -> Unit,
) : CircuitUiState

sealed interface LoginSideEffect {
    data object KakaoLogin : LoginSideEffect
    data class ShowToast(val message: String) : LoginSideEffect
}

sealed interface LoginUiEvent : CircuitUiEvent {
    data object InitSideEffect : LoginUiEvent
    data object OnKakaoLoginButtonClick : LoginUiEvent
    data class Login(val accessToken: String) : LoginUiEvent
    data class LoginFailure(val message: String) : LoginUiEvent
}
