package com.ninecraft.booket.core.model

data class TermsAgreementModel(
    val id: String,
    val email: String,
    val nickname: String,
    val provider: String,
    val termsAgreed: Boolean,
)
