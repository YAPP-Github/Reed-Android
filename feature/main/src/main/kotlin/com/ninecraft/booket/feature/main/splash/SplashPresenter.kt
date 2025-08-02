package com.ninecraft.booket.feature.main.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val userRepository: UserRepository,
) : Presenter<SplashUiState> {

    @Composable
    override fun present(): SplashUiState {
        val onboardingState by userRepository.onboardingState.collectAsRetainedState(initial = OnboardingState.IDLE)
        val autoLoginState by userRepository.autoLoginState.collectAsRetainedState(initial = AutoLoginState.IDLE)

        RememberedEffect(onboardingState, autoLoginState) {
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
