package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
        val agreedTerms = rememberRetained { mutableStateListOf(false, false, false) }

        val isAllAgreed by remember {
            derivedStateOf {
                agreedTerms.all { it }
            }
        }

        val isStartButtonEnabled by remember {
            derivedStateOf {
                isAllAgreed
            }
        }

        fun handleEvent(event: TermsAgreementScreen.Event) {
            when (event) {
                is TermsAgreementScreen.Event.OnAllTermsAgreedClick -> {
                    val toggleAgreed = !isAllAgreed
                    for (i in agreedTerms.indices) {
                        agreedTerms[i] = toggleAgreed
                    }
                }

                is TermsAgreementScreen.Event.OnTermItemClick -> {
                    agreedTerms[event.index] = !agreedTerms[event.index]
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
