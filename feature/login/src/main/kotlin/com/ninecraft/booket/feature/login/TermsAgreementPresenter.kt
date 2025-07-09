package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

class TermsAgreementPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<TermsAgreementScreen.State> {

    @Composable
    override fun present(): TermsAgreementScreen.State {
        var agreedTerms by rememberRetained {
            mutableStateOf(persistentListOf(false, false, false))
        }

        val isAllAgreed by remember {
            derivedStateOf {
                agreedTerms.all { it }
            }
        }

        fun handleEvent(event: TermsAgreementScreen.Event) {
            when (event) {
                is TermsAgreementScreen.Event.OnAllTermsAgreedClick -> {
                    val toggleAgreed = !isAllAgreed
                    agreedTerms = agreedTerms.map { toggleAgreed }.toPersistentList()
                }

                is TermsAgreementScreen.Event.OnTermItemClick -> {
                    agreedTerms = agreedTerms.set(event.index, !agreedTerms[event.index])
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
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(TermsAgreementScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): TermsAgreementPresenter
    }
}
