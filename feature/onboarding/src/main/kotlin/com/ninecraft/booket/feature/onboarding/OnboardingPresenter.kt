
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.feature.onboarding.OnboardingUiEvent
import com.ninecraft.booket.feature.onboarding.OnboardingUiState
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

@Suppress("unused")
class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<OnboardingUiState> {

    @Composable
    override fun present(): OnboardingUiState {
        val scope = rememberCoroutineScope()

        fun handleEvent(event: OnboardingUiEvent) {
            when (event) {
                is OnboardingUiEvent.OnNextButtonClick -> {
                    navigator.resetRoot(HomeScreen)
                }
            }
        }

        return OnboardingUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
