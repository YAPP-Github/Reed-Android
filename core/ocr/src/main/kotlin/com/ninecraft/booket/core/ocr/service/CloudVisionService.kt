package com.ninecraft.booket.core.ocr.service

import com.ninecraft.booket.core.ocr.model.CloudVisionRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CloudVisionService {
    @POST("v1/images:annotate")
    suspend fun annotate(
        @Query("key") key: String,
        @Body body: CloudVisionRequest
    ): CloudVisionResponse
}
