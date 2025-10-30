package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.utils.postErrorDialog
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.TermsAgreementScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class LoginPresenter @AssistedInject constructor(
    @Assisted private val screen: LoginScreen,
    @Assisted private val navigator: Navigator,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<LoginUiState> {

    companion object {
        private const val EVENT_ERROR_LOGIN = "error_login"
    }

    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LoginSideEffect?>(null) }

        fun navigateAfterLogin() {
            scope.launch {
                userRepository.getUserProfile()
                    .onSuccess { userProfile ->
                        if (userProfile.termsAgreed) {
                            if (screen.returnToScreen == null) {
                                navigator.resetRoot(HomeScreen)
                            } else {
                                navigator.popUntil { it == screen.returnToScreen }
                            }
                        } else {
                            if (screen.returnToScreen == null) {
                                navigator.resetRoot(TermsAgreementScreen())
                            } else {
                                navigator.goTo(TermsAgreementScreen(screen.returnToScreen))
                            }
                        }
                    }.onFailure { exception ->
                        Logger.e(exception.message ?: "Failed to get user profile")
                        postErrorDialog(
                            errorScope = ErrorScope.LOGIN,
                            exception = exception,
                        )
                    }
            }
        }

        fun handleEvent(event: LoginUiEvent) {
            when (event) {
                is LoginUiEvent.OnKakaoLoginButtonClick -> {
                    isLoading = true
                    sideEffect = LoginSideEffect.KakaoLogin()
                }

                is LoginUiEvent.LoginFailure -> {
                    isLoading = false
                    analyticsHelper.logEvent(EVENT_ERROR_LOGIN)
                    sideEffect = LoginSideEffect.ShowToast(event.message)
                }

                is LoginUiEvent.Login -> {
                    scope.launch {
                        try {
                            isLoading = true
                            authRepository.login(event.accessToken)
                                .onSuccess {
                                    userRepository.syncFcmToken()
                                    navigateAfterLogin()
                                }.onFailure { exception ->
                                    Logger.e(exception.message ?: "Login failed")
                                    analyticsHelper.logEvent(EVENT_ERROR_LOGIN)
                                    postErrorDialog(
                                        errorScope = ErrorScope.LOGIN,
                                        exception = exception,
                                    )
                                }
                        } finally {
                            isLoading = false
                        }
                    }
                }

                is LoginUiEvent.OnGuestLoginButtonClick -> {
                    navigator.resetRoot(HomeScreen)
                }

                is LoginUiEvent.OnCloseButtonClick -> {
                    navigator.pop()
                }
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(screen.name)
        }

        return LoginUiState(
            isLoading = isLoading,
            returnToScreen = screen.returnToScreen,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: LoginScreen,
            navigator: Navigator,
        ): LoginPresenter
    }
}
