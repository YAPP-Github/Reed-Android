package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.service.BooketService
import javax.inject.Inject

private const val KAKAO_PROVIDER_TYPE = "KAKAO"

internal class DefaultAuthRepository @Inject constructor(
    private val service: BooketService,
) : AuthRepository {
    override suspend fun login(accessToken: String) = runSuspendCatching {
        service.login(
            LoginRequest(
                providerType = KAKAO_PROVIDER_TYPE,
                oauthToken = accessToken
            )
        ).toModel()
    }
}

