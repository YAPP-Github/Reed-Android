package com.ninecraft.booket.feature.termsagreement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.WebViewConstants
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.login.LoginSideEffect
import com.ninecraft.booket.feature.screens.BottomNavigationScreen
import com.ninecraft.booket.feature.screens.OnboardingScreen
import com.ninecraft.booket.feature.screens.TermsAgreementScreen
import com.ninecraft.booket.feature.screens.WebViewScreen
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TermsAgreementPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : Presenter<TermsAgreementUiState> {

    @Composable
    override fun present(): TermsAgreementUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by rememberRetained { mutableStateOf<TermsAgreementSideEffect?>(null) }

        var agreedTerms by rememberRetained {
            mutableStateOf(persistentListOf(false, false, false))
        }

        val isAllAgreed by remember {
            derivedStateOf {
                agreedTerms.all { it }
            }
        }

        val isOnboardingCompleted by userRepository.isOnboardingCompleted.collectAsRetainedState(initial = false)

        fun handleEvent(event: TermsAgreementUiEvent) {
            when (event) {
                is TermsAgreementUiEvent.OnAllTermsAgreedClick -> {
                    val toggleAgreed = !isAllAgreed
                    agreedTerms = agreedTerms.map { toggleAgreed }.toPersistentList()
                }

                is TermsAgreementUiEvent.OnTermItemClick -> {
                    agreedTerms = agreedTerms.set(event.index, !agreedTerms[event.index])
                }

                is TermsAgreementUiEvent.OnPolicyClick -> {
                    val policy = WebViewConstants.PRIVACY_POLICY
                    navigator.goTo(WebViewScreen(url = policy.url, title = policy.title))
                }

                is TermsAgreementUiEvent.OnTermClick -> {
                    val terms = WebViewConstants.TERMS_OF_SERVICE
                    navigator.goTo(WebViewScreen(url = terms.url, title = terms.title))
                }

                is TermsAgreementUiEvent.OnStartButtonClick -> {
                    scope.launch {
                        authRepository.agreeTerms(true)
                            .onSuccess {
                                if (isOnboardingCompleted) {
                                    navigator.resetRoot(BottomNavigationScreen)
                                } else {
                                    navigator.resetRoot(OnboardingScreen)
                                }
                            }.onFailure { exception ->
                                exception.message?.let { Logger.e(it) }
                                sideEffect = exception.message?.let {
                                    TermsAgreementSideEffect.ShowToast(it)
                                }
                            }
                    }
                }
            }
        }

        return TermsAgreementUiState(
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
