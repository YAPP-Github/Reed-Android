package com.ninecraft.booket.core.network.service

import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.request.LoginRequest
import com.ninecraft.booket.core.network.request.RecordRegisterRequest
import com.ninecraft.booket.core.network.request.RefreshTokenRequest
import com.ninecraft.booket.core.network.request.TermsAgreementRequest
import com.ninecraft.booket.core.network.response.BookDetailResponse
import com.ninecraft.booket.core.network.response.BookSearchResponse
import com.ninecraft.booket.core.network.response.BookUpsertResponse
import com.ninecraft.booket.core.network.response.HomeResponse
import com.ninecraft.booket.core.network.response.LibraryResponse
import com.ninecraft.booket.core.network.response.LoginResponse
import com.ninecraft.booket.core.network.response.RecordDetailResponse
import com.ninecraft.booket.core.network.response.RecordRegisterResponse
import com.ninecraft.booket.core.network.response.RefreshTokenResponse
import com.ninecraft.booket.core.network.response.TermsAgreementResponse
import com.ninecraft.booket.core.network.response.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReedService {
    // Auth endpoints (no auth required)
    @POST("api/v1/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse

    // Auth endpoints (auth required)
    @POST("api/v1/auth/signout")
    suspend fun logout()

    @PUT("api/v1/auth/terms-agreement")
    suspend fun agreeTerms(@Body termsAgreementRequest: TermsAgreementRequest): TermsAgreementResponse

    @GET("api/v1/auth/me")
    suspend fun getUserProfile(): UserProfileResponse

    // Book endpoints (auth required)
    @GET("api/v1/books/search")
    suspend fun searchBook(
        @Query("query") query: String,
        @Query("queryType") queryType: String = "Title",
        @Query("searchTarget") searchTarget: String = "Book",
        @Query("maxResults") maxResults: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "Accuracy",
        @Query("cover") cover: String? = "Big",
        @Query("categoryId") categoryId: Int = 0,
    ): BookSearchResponse

    @GET("api/v1/books/detail")
    suspend fun getBookDetail(
        @Query("itemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN",
        @Query("optResult") optResult: String = "BookInfo,Toc,PreviewImg",
    ): BookDetailResponse

    @PUT("api/v1/books/upsert")
    suspend fun upsertBook(@Body bookUpsertRequest: BookUpsertRequest): BookUpsertResponse

    @GET("api/v1/books/my-library")
    suspend fun getLibraryBooks(
        @Query("status") status: String? = null,
        @Query("title") title: String? = null,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String = "CREATED_DATE_DESC",
    ): LibraryResponse

    // Reading-records endpoints (auth required)
    @POST("api/v1/reading-records/{userBookId}")
    suspend fun postRecord(
        @Path("userBookId") userBookId: String,
        @Body recordRegisterRequest: RecordRegisterRequest,
    ): RecordRegisterResponse

    @GET("api/v1/reading-records/detail/{readingRecordId}")
    suspend fun getRecordDetail(
        @Path("readingRecordId") readingRecordId: String,
    ): RecordDetailResponse

    // Home (auth required)
    @GET("api/v1/home")
    suspend fun getHome(
        @Query("limit") limit: Int = 3,
    ): HomeResponse
}
