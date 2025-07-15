package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.request.RefreshTokenRequest
import com.ninecraft.booket.core.network.response.BookDetailResponse
import com.ninecraft.booket.core.network.response.BookSearchResponse
import com.ninecraft.booket.core.network.response.LoginResponse
import com.ninecraft.booket.core.network.response.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoAuthService {
    @POST("api/v1/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    @GET("api/v1/books/search")
    suspend fun searchBook(
        @Query("query") query: String,
        @Query("queryType") queryType: String = "Title",
        @Query("searchTarget") searchTarget: String? = null,
        @Query("maxResults") maxResults: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "Accuracy",
        @Query("cover") cover: String? = "Big",
        @Query("categoryId") categoryId: Int? = null,
    ): BookSearchResponse

    @GET("api/v1/books/detail")
    suspend fun getBookDetail(
        @Query("itemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN",
        @Query("optResult") optResult: String = "BookInfo,Toc,PreviewImg",
    ): BookDetailResponse
}
