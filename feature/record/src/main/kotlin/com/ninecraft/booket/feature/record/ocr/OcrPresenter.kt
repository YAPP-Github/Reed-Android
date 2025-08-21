package com.ninecraft.booket.feature.record.ocr

import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.ocr.analyzer.CloudOcrRecognizer
import com.ninecraft.booket.feature.screens.OcrScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class OcrPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val recognizer: CloudOcrRecognizer,
) : Presenter<OcrUiState> {

    @Composable
    override fun present(): OcrUiState {
        val scope = rememberCoroutineScope()
        var currentUi by rememberRetained { mutableStateOf(OcrUi.CAMERA) }
        var isPermissionDialogVisible by rememberRetained { mutableStateOf(false) }
        var sentenceList by rememberRetained { mutableStateOf(persistentListOf<String>()) }
        var recognizedText by rememberRetained { mutableStateOf("") }
        var selectedIndices by rememberRetained { mutableStateOf(setOf<Int>()) }
        var mergedSentence by rememberRetained { mutableStateOf("") }
        var isTextDetectionFailed by rememberRetained { mutableStateOf(false) }
        var isRecaptureDialogVisible by rememberRetained { mutableStateOf(false) }
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<OcrSideEffect?>(null) }

        fun recognizeText(base64Image: String) {
            scope.launch {
                try {
                    isLoading = true
                    recognizer.recognizeText(base64Image)
                        .onSuccess {
                            val text = it.responses.firstOrNull()?.fullTextAnnotation?.text.orEmpty()
                            recognizedText = text

                            if (text.isNotBlank()) {
                                isTextDetectionFailed = false
                                val sentences = text
                                    .split("\n")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }

                                sentenceList = sentences.toPersistentList()
                                currentUi = OcrUi.RESULT
                            } else {
                                isTextDetectionFailed = true
                            }
                        }
                        .onFailure { exception ->
                            isTextDetectionFailed = true

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

                is OcrUiEvent.OnCaptureButtonClick -> {
                    isTextDetectionFailed = false

                    val base64Image = Base64.encodeToString(event.imageData, Base64.NO_WRAP)
                    recognizeText(base64Image)
                }

                is OcrUiEvent.OnReCaptureButtonClick -> {
                    isRecaptureDialogVisible = true
                }

                is OcrUiEvent.OnSelectionConfirmed -> {
                    mergedSentence = selectedIndices
                        .sorted().joinToString(" ") { sentenceList[it] }
                    navigator.pop(result = OcrScreen.OcrResult(mergedSentence))
                }

                is OcrUiEvent.OnSentenceSelected -> {
                    selectedIndices = if (selectedIndices.contains(event.index)) {
                        selectedIndices - event.index
                    } else {
                        selectedIndices + event.index
                    }
                }

                is OcrUiEvent.OnRecaptureDialogConfirmed -> {
                    selectedIndices = emptySet()
                    isRecaptureDialogVisible = false
                    currentUi = OcrUi.CAMERA
                }

                is OcrUiEvent.OnRecaptureDialogDismissed -> {
                    isRecaptureDialogVisible = false
                }
            }
        }

        return OcrUiState(
            currentUi = currentUi,
            isPermissionDialogVisible = isPermissionDialogVisible,
            sentenceList = sentenceList,
            selectedIndices = selectedIndices,
            isTextDetectionFailed = isTextDetectionFailed,
            isRecaptureDialogVisible = isRecaptureDialogVisible,
            isLoading = isLoading,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OcrPresenter
    }
}
