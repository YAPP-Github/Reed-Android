package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.response.BookUpsertResponse
import com.ninecraft.booket.core.network.response.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST("api/v1/auth/signout")
    suspend fun logout()

    @GET("api/v1/auth/me")
    suspend fun getUserProfile(): UserProfileResponse

    @PUT("api/v1/books/upsert")
    suspend fun upsertBook(bookUpsertRequest: BookUpsertRequest): BookUpsertResponse
}
