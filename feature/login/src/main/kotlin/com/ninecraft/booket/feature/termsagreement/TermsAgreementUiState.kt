package com.ninecraft.booket.feature.termsagreement

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

data class TermsAgreementUiState(
    val isAllAgreed: Boolean,
    val agreedTerms: ImmutableList<Boolean>,
    val eventSink: (TermsAgreementUiEvent) -> Unit,
) : CircuitUiState

sealed interface TermsAgreementUiEvent : CircuitUiEvent {
    data object OnAllTermsAgreedClick : TermsAgreementUiEvent
    data class OnTermItemClick(val index: Int) : TermsAgreementUiEvent
    data object OnBackClick : TermsAgreementUiEvent
    data object OnPolicyClick : TermsAgreementUiEvent
    data object OnTermClick : TermsAgreementUiEvent
    data object OnStartButtonClick : TermsAgreementUiEvent
}
