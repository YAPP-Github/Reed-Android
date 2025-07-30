package com.ninecraft.booket.core.data.api.repository

interface AuthRepository {
    suspend fun login(accessToken: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun agreeTerms(termsAgreed: Boolean): Result<Unit>
}
