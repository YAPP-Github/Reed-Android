package com.ninecraft.booket.core.ocr.service

import com.ninecraft.booket.core.ocr.model.CloudVisionRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CloudVisionService {
    @POST("v1/images:annotate")
    suspend fun batchAnnotateImage(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Body body: CloudVisionRequest,
    ): CloudVisionResponse
}
