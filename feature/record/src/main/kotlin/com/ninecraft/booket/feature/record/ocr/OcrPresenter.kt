package com.ninecraft.booket.feature.record.ocr

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.ocr.recognizer.CloudOcrRecognizer
import com.ninecraft.booket.feature.record.ocr.OcrSideEffect.ShowToast
import com.ninecraft.booket.feature.screens.OcrScreen
import com.ninecraft.booket.feature.screens.OcrScreen.OcrResult
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AssistedInject
class OcrPresenter(
    @Assisted private val navigator: Navigator,
    private val recognizer: CloudOcrRecognizer,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<OcrUiState> {

    @CircuitInject(OcrScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OcrPresenter
    }

    companion object {
        private const val RECORD_OCR_SENTENCE = "record_OCR_sentence"
        private const val CAMERA_MAX_FAILURES = 2
    }

    @Composable
    override fun present(): OcrUiState {
        val scope = rememberCoroutineScope()
        var currentUi by rememberRetained { mutableStateOf(OcrUi.CAMERA) }
        var isPermissionDialogVisible by rememberRetained { mutableStateOf(false) }
        var selectedImage by rememberRetained { mutableStateOf("") }
        var sentenceList by rememberRetained { mutableStateOf(persistentListOf<String>()) }
        var selectedIndices by rememberRetained { mutableStateOf(persistentSetOf<Int>()) }
        var mergedSentence by rememberRetained { mutableStateOf("") }
        var isTextDetectionFailed by rememberRetained { mutableStateOf(false) }
        var isCameraRecognitionFailedDialogVisible by rememberRetained { mutableStateOf(false) }
        var isGalleryRecognitionFailedDialogVisible by rememberRetained { mutableStateOf(false) }
        var isRecaptureDialogVisible by rememberRetained { mutableStateOf(false) }
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<OcrSideEffect?>(null) }

        var cameraFailureCount by rememberRetained { mutableStateOf(0) }

        LaunchedEffect(isTextDetectionFailed) {
            if (isTextDetectionFailed) {
                delay(2000)
                isTextDetectionFailed = false
            }
        }

        fun handleRecognitionSuccess(text: String) {
            isTextDetectionFailed = false
            cameraFailureCount = 0

            val sentences = text
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            sentenceList = sentences.toPersistentList()
            currentUi = OcrUi.RESULT
            analyticsHelper.logScreenView(RECORD_OCR_SENTENCE)
        }

        fun handleRecognitionFailure(source: RecognizeSource) {
            when (source) {
                RecognizeSource.CAMERA -> {
                    isTextDetectionFailed = true
                    cameraFailureCount += 1

                    if (cameraFailureCount > CAMERA_MAX_FAILURES) {
                        isCameraRecognitionFailedDialogVisible = true
                    }
                }

                RecognizeSource.GALLERY -> {
                    isGalleryRecognitionFailedDialogVisible = true
                }
            }
        }

        fun recognizeText(imageUri: Uri, source: RecognizeSource) {
            scope.launch {
                try {
                    isLoading = true
                    recognizer.recognizeText(imageUri)
                        .onSuccess {
                            val text = it.responses.firstOrNull()?.fullTextAnnotation?.text.orEmpty()

                            if (text.isNotBlank()) {
                                handleRecognitionSuccess(text)
                            } else {
                                handleRecognitionFailure(source)
                            }
                        }
                        .onFailure { exception ->
                            handleRecognitionFailure(source)

                            val handleErrorMessage = { message: String ->
                                Logger.e("Cloud Vision API Error: ${exception.message}")
                                sideEffect = OcrSideEffect.ShowToast(message)
                            }

                            handleException(
                                exception = exception,
                                onError = handleErrorMessage,
                                onLoginRequired = {},
                            )
                        }
                } finally {
                    isLoading = false
                }
            }
        }

        fun handleEvent(event: OcrUiEvent) {
            when (event) {
                is OcrUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                is OcrUiEvent.OnShowPermissionDialog -> {
                    isPermissionDialogVisible = true
                }

                is OcrUiEvent.OnHidePermissionDialog -> {
                    isPermissionDialogVisible = false
                }

                is OcrUiEvent.OnCaptureStart -> {
                    isLoading = true
                }

                is OcrUiEvent.OnCaptureFailed -> {
                    isLoading = false
                    sideEffect = ShowToast("이미지 캡처에 실패했어요")
                    Logger.e("ImageCaptureException: ${event.exception.message}")
                }

                is OcrUiEvent.OnImageCaptured -> {
                    isTextDetectionFailed = false

                    recognizeText(event.imageUri, RecognizeSource.CAMERA)
                }

                is OcrUiEvent.OnImageSelected -> {
                    currentUi = OcrUi.IMAGE
                    selectedImage = event.imageUri
                    isTextDetectionFailed = false
                    cameraFailureCount = 0

                    val pareUri = selectedImage.toUri()
                    recognizeText(pareUri, RecognizeSource.GALLERY)
                }

                is OcrUiEvent.OnReCaptureButtonClick -> {
                    isRecaptureDialogVisible = true
                }

                is OcrUiEvent.OnSelectionConfirmed -> {
                    mergedSentence = selectedIndices
                        .sorted().joinToString("") { sentenceList[it] }
                    navigator.pop(result = OcrResult(mergedSentence))
                }

                is OcrUiEvent.OnSentenceSelected -> {
                    selectedIndices = if (selectedIndices.contains(event.index)) {
                        selectedIndices - event.index
                    } else {
                        selectedIndices + event.index
                    }.toPersistentSet()
                }

                is OcrUiEvent.OnRecaptureDialogConfirmed -> {
                    selectedIndices = persistentSetOf()
                    isRecaptureDialogVisible = false
                    currentUi = OcrUi.CAMERA
                }

                is OcrUiEvent.OnRecaptureDialogDismissed -> {
                    isRecaptureDialogVisible = false
                }

                OcrUiEvent.OnImageContentClosed -> {
                    currentUi = OcrUi.CAMERA
                }

                OcrUiEvent.OnCameraRecognitionFailedDialogDismissed -> {
                    isCameraRecognitionFailedDialogVisible = false
                    cameraFailureCount = 0
                }

                OcrUiEvent.OnImageRecognitionFailedDialogDismissed -> {
                    isGalleryRecognitionFailedDialogVisible = false
                }
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(OcrScreen.name)
        }

        return OcrUiState(
            currentUi = currentUi,
            isPermissionDialogVisible = isPermissionDialogVisible,
            selectedImage = selectedImage,
            sentenceList = sentenceList,
            selectedIndices = selectedIndices,
            isTextDetectionFailed = isTextDetectionFailed,
            isCameraRecognitionFailedDialogVisible = isCameraRecognitionFailedDialogVisible,
            isGalleryRecognitionFailedDialogVisible = isGalleryRecognitionFailedDialogVisible,
            isRecaptureDialogVisible = isRecaptureDialogVisible,
            isLoading = isLoading,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
