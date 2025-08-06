package com.ninecraft.booket.feature.main.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.model.AutoLoginState
import com.ninecraft.booket.core.model.OnboardingState
import com.ninecraft.booket.feature.screens.BottomNavigationScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.ninecraft.booket.feature.screens.SplashScreen
import com.skydoves.compose.effects.RememberedEffect
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

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {
        val onboardingState by userRepository.onboardingState.collectAsRetainedState(initial = OnboardingState.IDLE)
        val autoLoginState by authRepository.autoLoginState.collectAsRetainedState(initial = AutoLoginState.IDLE)
        var isSplashTimeCompleted by rememberRetained { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(3000L)
            isSplashTimeCompleted = true
        }

        RememberedEffect(onboardingState, autoLoginState, isSplashTimeCompleted) {
            if (!isSplashTimeCompleted) return@RememberedEffect

            when (onboardingState) {
                OnboardingState.NOT_COMPLETED -> {
                    navigator.resetRoot(OnboardingScreen)
                }

                OnboardingState.COMPLETED -> {
                    when (autoLoginState) {
                        AutoLoginState.LOGGED_IN -> {
                            navigator.resetRoot(BottomNavigationScreen)
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

        return SplashUiState(
            idle = onboardingState == OnboardingState.IDLE || autoLoginState == AutoLoginState.IDLE,
            onboardingState = onboardingState,
            autoLoginState = autoLoginState,
        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}
