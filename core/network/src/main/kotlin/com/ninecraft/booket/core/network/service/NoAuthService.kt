package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.request.RefreshTokenRequest
import com.ninecraft.booket.core.network.response.LoginResponse
import com.ninecraft.booket.core.network.response.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NoAuthService {
    @POST("api/v1/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse
}
