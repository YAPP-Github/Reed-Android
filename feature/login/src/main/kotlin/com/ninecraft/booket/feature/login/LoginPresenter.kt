package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.BottomNavigationScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : Presenter<LoginUiState> {

    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LoginSideEffect?>(null) }
        val isOnboardingCompleted by userRepository.isOnboardingCompleted.collectAsRetainedState(
            initial = false
        )

        fun navigateAfterLogin() {
            scope.launch {
                userRepository.getUserProfile()
                    .onSuccess { userProfile ->
                        if (userProfile.termsAgreed) {
                            if (isOnboardingCompleted) {
                                navigator.resetRoot(BottomNavigationScreen)
                            } else {
                                navigator.resetRoot(OnboardingScreen)
                            }
                        } else {
                            navigator.resetRoot(TermsAgreementScreen)
                        }
                    }.onFailure { exception ->
                        exception.message?.let { Logger.e(it) }
                        sideEffect = exception.message?.let {
                            LoginSideEffect.ShowToast(it)
                        }
                    }
            }
        }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
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
                            authRepository.login(event.accessToken)
                                .onSuccess {
                                    navigateAfterLogin()
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
