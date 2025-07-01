package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/v1/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}
