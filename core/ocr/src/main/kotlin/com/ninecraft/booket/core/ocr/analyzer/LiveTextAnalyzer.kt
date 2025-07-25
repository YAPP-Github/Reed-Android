package com.ninecraft.booket.core.ocr.analyzer

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 실시간 카메라 스트림에서 프레임 단위로 텍스트 분석하는 Analyzer 클래스
 *
 * ML Kit의 TextRecognizer를 사용하여 `InputImage` 객체로부터 텍스트를 추출하고
 * 성공 시 [onTextDetected], 실패 시 [onFailure], 분석 완료 후 공통적으로 [onRecognitionCompleted] 콜백을 호출
 *
 * 안정적인 연속 프레임 분석을 위해 CoroutineScope에 [SupervisorJob]을 사용하여
 * 한 프레임 분석에서 예외가 발생해도 다음 프레임 분석에 영향을 주지 않도록 설계
 *
 * [onRecognitionCompleted]에서는 반드시 imageProxy.close()를 호출해야 하므로
 * 예외 여부와 관계없이 항상 호출되도록 [invokeOnCompletion]에서 실행
 */
class LiveTextAnalyzer @Inject constructor(
    private val textRecognizer: TextRecognizer,
    private val onTextDetected: (String) -> Unit,
    private val onFailure: () -> Unit,
    private val onRecognitionCompleted: () -> Unit,
) : TextAnalyzer {

    companion object {
        const val THROTTLE_TIMEOUT_MS = 1_000L // 프레임 처리 간 인터벌
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun analyze(inputImage: InputImage) {
        scope.launch {
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
            delay(THROTTLE_TIMEOUT_MS)
        }.invokeOnCompletion { exception ->
            Logger.e(exception?.message ?: "Unknown error")
            onRecognitionCompleted() // imageProxy.close() 호출
        }
    }
}
