package com.ninecraft.booket.core.ocr.analyzer

import com.ninecraft.booket.core.ocr.model.AnnotateImageRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionRequest
import com.ninecraft.booket.core.ocr.model.Feature
import com.ninecraft.booket.core.ocr.model.ImageContext
import com.ninecraft.booket.core.ocr.model.VisionImage
import com.ninecraft.booket.core.ocr.service.CloudVisionService
import com.orhanobut.logger.Logger
import javax.inject.Inject

class CloudOcrRecognizer @Inject constructor(
    private val service: CloudVisionService,
) {
    suspend fun recognizeText(base64Image: String) {
        try {
            val request = CloudVisionRequest(
                requests = listOf(
                    AnnotateImageRequest(
                        image = VisionImage(base64Image),
                        features = listOf(Feature(type = "TEXT_DETECTION")),
                        imageContext = ImageContext(languageHints = null),
                    ),
                ),
            )
            service.annotate(
                key = "", // local properties 에서 key 가져오기
                body = request,
            )
        } catch (e: Exception) {
            Logger.e("${e.message}")
        }
    }
}
