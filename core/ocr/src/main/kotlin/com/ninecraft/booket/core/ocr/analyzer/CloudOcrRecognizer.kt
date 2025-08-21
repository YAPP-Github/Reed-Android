package com.ninecraft.booket.core.ocr.analyzer

import android.net.Uri
import android.util.Base64
import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.ocr.BuildConfig
import com.ninecraft.booket.core.ocr.model.AnnotateImageRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionRequest
import com.ninecraft.booket.core.ocr.model.CloudVisionResponse
import com.ninecraft.booket.core.ocr.model.Feature
import com.ninecraft.booket.core.ocr.model.ImageContext
import com.ninecraft.booket.core.ocr.model.VisionImage
import com.ninecraft.booket.core.ocr.service.CloudVisionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class CloudOcrRecognizer @Inject constructor(
    private val service: CloudVisionService,
) {
    suspend fun recognizeText(imageUri: Uri): Result<CloudVisionResponse> = runSuspendCatching {
        withContext(Dispatchers.IO) {
            val filePath = imageUri.path ?: throw IllegalArgumentException("URI does not have a valid path.")
            val file = File(filePath)
            val byte = file.readBytes()
            val base64Image = Base64.encodeToString(byte, Base64.NO_WRAP)

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
                apiKey = BuildConfig.CLOUD_VISION_API_KEY,
                body = request,
            )
        }
    }
}
