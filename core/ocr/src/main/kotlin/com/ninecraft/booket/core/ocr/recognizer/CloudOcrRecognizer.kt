package com.ninecraft.booket.core.ocr.recognizer

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.di.ApplicationContext
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
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import com.ninecraft.booket.core.di.DataScope

@SingleIn(DataScope::class)
@Inject
class CloudOcrRecognizer(
    @param: ApplicationContext private val context: Context,
    private val service: CloudVisionService,
) {
    suspend fun recognizeText(imageUri: Uri): Result<CloudVisionResponse> = runSuspendCatching {
        withContext(Dispatchers.IO) {
            val byte = when (imageUri.scheme) {
                null, "file" -> {
                    val filePath = imageUri.path ?: throw IllegalArgumentException("URI does not have a valid path.")
                    val file = File(filePath)
                    file.readBytes()
                }
                else -> {
                    context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }
                        ?: throw IllegalArgumentException("Unable to open image input stream.")
                }
            }
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
