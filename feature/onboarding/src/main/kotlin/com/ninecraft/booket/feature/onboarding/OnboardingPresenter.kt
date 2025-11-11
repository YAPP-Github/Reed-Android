
package com.ninecraft.booket.feature.onboarding

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

const val ONBOARDING_STEPS_COUNT = 3

@Inject
class OnboardingPresenter(
    @Assisted private val navigator: Navigator,
    private val repository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<OnboardingUiState> {

    @CircuitInject(OnboardingScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OnboardingPresenter
    }

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
                            navigator.resetRoot(LoginScreen())
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

        ImpressionEffect {
            analyticsHelper.logScreenView(OnboardingScreen.name)
        }

        return OnboardingUiState(
            pagerState = pagerState,
            eventSink = ::handleEvent,
        )
    }
}
