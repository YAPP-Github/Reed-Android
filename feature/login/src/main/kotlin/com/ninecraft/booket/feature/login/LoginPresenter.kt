package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.TermsAgreementScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: AuthRepository,
) : Presenter<LoginUiState> {

    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LoginSideEffect?>(null) }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    isLoading = true
                    sideEffect = LoginSideEffect.KakaoLogin
                }

                is LoginUiEvent.LoginFailure -> {
                    isLoading = false
                    sideEffect = LoginSideEffect.ShowToast(event.message)
                }

                is LoginUiEvent.Login -> {
                    scope.launch {
                        try {
                            isLoading = true
                            repository.login(event.accessToken)
                                .onSuccess {
                                    navigator.resetRoot(TermsAgreementScreen)
                                }.onFailure { exception ->
                                    exception.message?.let { Logger.e(it) }
                                    sideEffect = exception.message?.let {
                                        LoginSideEffect.ShowToast(it)
                                    }
                                }
                        } finally {
                            isLoading = false
                        }
                    }
                }
            }
        }

        return LoginUiState(
            isLoading = isLoading,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}
