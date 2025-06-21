package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.response.HealthCheckResponse
import retrofit2.http.GET

interface BooketService {
    @GET("health")
    suspend fun checkServerHealth(): HealthCheckResponse
}
