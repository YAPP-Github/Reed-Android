package com.ninecraft.booket.core.network.service

import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/signout")
    suspend fun logout()
}
