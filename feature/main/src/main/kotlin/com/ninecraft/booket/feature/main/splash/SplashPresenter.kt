package com.ninecraft.booket.feature.main.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.model.OnboardingState
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

        RememberedEffect(onboardingState) {
            when (onboardingState) {
                OnboardingState.IDLE -> {
                    // 초기 진입 상태
                }

                OnboardingState.NOT_COMPLETED -> {
                    navigator.resetRoot(OnboardingScreen)
                }

                OnboardingState.COMPLETED -> {
                    navigator.resetRoot(LoginScreen)
                }
            }
        }

        return SplashUiState(
            idle = onboardingState == OnboardingState.IDLE,
            isOnboardingCompleted = when (onboardingState) {
                OnboardingState.IDLE -> null
                OnboardingState.NOT_COMPLETED -> false
                OnboardingState.COMPLETED -> true
            },
        )
    }

    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}
