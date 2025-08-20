package com.ninecraft.booket.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.ErrorScope
import com.ninecraft.booket.core.common.utils.postErrorDialog
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.model.OnboardingState
import com.ninecraft.booket.core.ui.R
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.ninecraft.booket.feature.screens.SplashScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) : Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {
        val scope = rememberCoroutineScope()
        val onboardingState by userRepository.onboardingState.collectAsRetainedState(initial = OnboardingState.IDLE)
        val autoLoginState by authRepository.autoLoginState.collectAsRetainedState(initial = AutoLoginState.IDLE)
        var isForceUpdateDialogVisible by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<SplashSideEffect?>(null) }

        fun checkTermsAgreement() {
            scope.launch {
                userRepository.getUserProfile()
                    .onSuccess { userProfile ->
                        if (userProfile.termsAgreed) {
                            navigator.resetRoot(HomeScreen)
                        } else {
                            navigator.resetRoot(LoginScreen)
                        }
                    }
                    .onFailure { exception ->
                        postErrorDialog(
                            errorScope = ErrorScope.GLOBAL,
                            exception = exception,
                            buttonLabelResId = R.string.retry,
                            action = { checkTermsAgreement() },
                        )
                    }
            }
        }

        fun proceedToNextScreen() {
            when (onboardingState) {
                OnboardingState.NOT_COMPLETED -> {
                    navigator.resetRoot(OnboardingScreen)
                }

                OnboardingState.COMPLETED -> {
                    when (autoLoginState) {
                        AutoLoginState.LOGGED_IN -> {
                            checkTermsAgreement()
                        }

                        AutoLoginState.NOT_LOGGED_IN -> {
                            navigator.resetRoot(LoginScreen)
                        }

                        AutoLoginState.IDLE -> {
                            // 자동 로그인 상태를 기다리는 중
                        }
                    }
                }

                OnboardingState.IDLE -> {
                    // 온보딩 상태를 기다리는 중
                }
            }
        }

        fun checkForceUpdate() {
            scope.launch {
                remoteConfigRepository.shouldUpdate()
                    .onSuccess { shouldUpdate ->
                        if (shouldUpdate) {
                            isForceUpdateDialogVisible = true
                        } else {
                            proceedToNextScreen()
                        }
                    }
                    .onFailure { exception ->
                        Logger.e("${exception.message}")
                        proceedToNextScreen()
                    }
            }
        }

        fun handleEvent(event: SplashUiEvent) {
            when (event) {
                SplashUiEvent.OnUpdateButtonClick -> {
                    sideEffect = SplashSideEffect.NavigateToPlayStore
                }

                SplashUiEvent.InitSideEffect -> {
                    sideEffect = null
                }
            }
        }

        LaunchedEffect(onboardingState, autoLoginState) {
            delay(1000L)

            if (onboardingState == OnboardingState.IDLE || autoLoginState == AutoLoginState.IDLE) {
                return@LaunchedEffect
            }

            checkForceUpdate()
        }

        return SplashUiState(
            isForceUpdateDialogVisible = isForceUpdateDialogVisible,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}
