package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.LoginMethod
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import java.util.UUID

data class LoginUiState(
    val isLoading: Boolean = false,
    val returnToScreen: Screen? = null,
    val sideEffect: LoginSideEffect? = null,
    val showLoginTooltip: Boolean = false,
    val recentLoginMethod: LoginMethod = LoginMethod.NONE,
    val eventSink: (LoginUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface LoginSideEffect {
    data class KakaoLogin(private val key: String = UUID.randomUUID().toString()) : LoginSideEffect
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : LoginSideEffect
}

sealed interface LoginUiEvent : CircuitUiEvent {
    data object OnKakaoLoginButtonClick : LoginUiEvent
    data class Login(val accessToken: String) : LoginUiEvent
    data class LoginFailure(val message: String) : LoginUiEvent
    data object OnGuestLoginButtonClick : LoginUiEvent
    data object OnCloseButtonClick : LoginUiEvent
    data object OnDismissLoginTooltip : LoginUiEvent
}
