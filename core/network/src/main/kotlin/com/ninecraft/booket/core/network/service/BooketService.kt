package com.ninecraft.booket.core.network.service

import retrofit2.http.POST

interface BooketService {
    @POST("api/v1/auth/signout")
    suspend fun logout()
}
