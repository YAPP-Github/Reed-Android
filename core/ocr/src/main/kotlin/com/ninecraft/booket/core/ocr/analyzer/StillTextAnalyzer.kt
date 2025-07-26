package com.ninecraft.booket.core.ocr.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.orhanobut.logger.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 정적인 카메라 이미지에서 텍스트를 분석하는 클래스
 *
 * CameraX의 단일 ImageProxy 프레임을 받아 ML Kit을 통해 텍스트를 추출하고 결과를 콜백으로 전달한다.
 *
 * @param textRecognizer ML Kit의 TextRecognizer 인스턴스
 * @param onTextDetected 텍스트 인식 성공 시 호출되는 콜백 (인식된 전체 텍스트 전달)
 * @param onFailure 인식 실패 시 호출되는 콜백
 *
 * 분석이 끝난 후 반드시 imageProxy.close() 호출하여 리소스 해제
 */
class StillTextAnalyzer @AssistedInject constructor(
    private val textRecognizer: TextRecognizer,
    @Assisted private val onTextDetected: (String) -> Unit,
    @Assisted private val onFailure: () -> Unit,
) : TextAnalyzer {

    val scope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnCompleteListener { visionText ->
                        onTextDetected(visionText.result.text)
                    }
                    .addOnFailureListener {
                        onFailure()
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }
        }.invokeOnCompletion { exception ->
            if (exception != null) {
                Logger.e(exception.message ?: "Unknown error")
            }
            imageProxy.close()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            onTextDetected: (String) -> Unit,
            onFailure: () -> Unit,
        ): StillTextAnalyzer
    }
}
