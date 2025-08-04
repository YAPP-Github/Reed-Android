package com.ninecraft.booket.feature.termsagreement

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import java.util.UUID

data class TermsAgreementUiState(
    val isAllAgreed: Boolean,
    val agreedTerms: ImmutableList<Boolean>,
    val sideEffect: TermsAgreementSideEffect? = null,
    val eventSink: (TermsAgreementUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface TermsAgreementSideEffect {
    data class ShowToast(
        val message: String,
        private val key: String = UUID.randomUUID().toString(),
    ) : TermsAgreementSideEffect
}

sealed interface TermsAgreementUiEvent : CircuitUiEvent {
    data object OnAllTermsAgreedClick : TermsAgreementUiEvent
    data class OnTermItemClick(val index: Int) : TermsAgreementUiEvent
    data object OnPolicyClick : TermsAgreementUiEvent
    data object OnTermClick : TermsAgreementUiEvent
    data object OnStartButtonClick : TermsAgreementUiEvent
}
