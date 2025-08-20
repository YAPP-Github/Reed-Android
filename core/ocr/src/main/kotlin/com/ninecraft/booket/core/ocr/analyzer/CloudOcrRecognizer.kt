package com.ninecraft.booket.core.ocr.analyzer

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.ocr.BuildConfig
import com.ninecraft.booket.core.ocr.model.AnnotateImageRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionResponse
import com.ninecraft.booket.core.ocr.model.Feature
import com.ninecraft.booket.core.ocr.model.ImageContext
import com.ninecraft.booket.core.ocr.model.VisionImage
import com.ninecraft.booket.core.ocr.service.CloudVisionService
import javax.inject.Inject

class CloudOcrRecognizer @Inject constructor(
    private val service: CloudVisionService,
) {
    suspend fun recognizeText(base64Image: String): Result<CloudVisionResponse> = runSuspendCatching {
        val request = CloudVisionRequest(
            requests = listOf(
                AnnotateImageRequest(
                    image = VisionImage(base64Image),
                    features = listOf(Feature(type = "TEXT_DETECTION")),
                    imageContext = ImageContext(languageHints = null),
                ),
            ),
        )

        service.batchAnnotateImage(
            key = BuildConfig.CLOUD_VISION_API_KEY,
            body = request,
        )
    }
}
