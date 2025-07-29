
package com.ninecraft.booket.feature.onboarding

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

const val ONBOARDING_STEPS_COUNT = 3

class OnboardingPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: UserRepository,
) : Presenter<OnboardingUiState> {

    @Composable
    override fun present(): OnboardingUiState {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { ONBOARDING_STEPS_COUNT })

        fun handleEvent(event: OnboardingUiEvent) {
            when (event) {
                is OnboardingUiEvent.OnNextButtonClick -> {
                    if (event.currentPage == 2) {
                        scope.launch {
                            repository.setOnboardingCompleted(true)
                            navigator.resetRoot(HomeScreen)
                        }
                    } else {
                        pagerState.let { state ->
                            scope.launch {
                                state.animateScrollToPage(event.currentPage + 1)
                            }
                        }
                    }
                }
            }
        }

        return OnboardingUiState(
            pagerState = pagerState,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OnboardingPresenter
    }
}
