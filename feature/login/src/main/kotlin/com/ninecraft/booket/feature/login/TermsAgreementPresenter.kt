package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.home.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class TermsAgreementPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<TermsAgreementScreen.State> {

    @Composable
    override fun present(): TermsAgreementScreen.State {
        var isAllAgreed by rememberRetained { mutableStateOf(false) }
        val agreedTerms = rememberRetained { mutableStateListOf(false, false, false) }
        var isStartButtonEnabled by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: TermsAgreementScreen.Event) {
            when (event) {
                is TermsAgreementScreen.Event.OnAllTermsAgreedClick -> {
                    isAllAgreed = !isAllAgreed
                    isStartButtonEnabled = isAllAgreed

                    for (i in agreedTerms.indices) {
                        agreedTerms[i] = isAllAgreed
                    }
                }
                is TermsAgreementScreen.Event.OnTermItemClick -> {
                    agreedTerms[event.index] = !agreedTerms[event.index]

                    val allIndividualAgreed = agreedTerms.all { it }

                    if (allIndividualAgreed) {
                        isAllAgreed = true
                        isStartButtonEnabled = true
                    } else {
                        isAllAgreed = false
                        isStartButtonEnabled = false
                    }
                }
                is TermsAgreementScreen.Event.OnBackClick -> {
                    navigator.pop()
                }
                is TermsAgreementScreen.Event.OnTermDetailClick -> {
                    // TODO: 웹뷰 화면으로 이동
                }
                is TermsAgreementScreen.Event.OnStartButtonClick -> {
                    navigator.resetRoot(HomeScreen)
                }
            }
        }

        return TermsAgreementScreen.State(
            isAllAgreed = isAllAgreed,
            agreedTerms = agreedTerms,
            isStartButtonEnabled = isStartButtonEnabled,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(TermsAgreementScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): TermsAgreementPresenter
    }
}
