package com.ninecraft.booket.feature.record.ocr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.ocr.analyzer.LiveTextAnalyzer
import com.ninecraft.booket.feature.screens.OcrScreen
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

class OcrPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val liveTextAnalyzer: LiveTextAnalyzer.Factory,
) : Presenter<OcrUiState> {

    @Composable
    override fun present(): OcrUiState {
        var currentUi by rememberRetained { mutableStateOf(OcrUi.CAMERA) }
        var isPermissionDialogVisible by rememberRetained { mutableStateOf(false) }
        var sentenceList by rememberRetained { mutableStateOf(emptyList<String>().toPersistentList()) }
        var recognizedText by rememberRetained { mutableStateOf("") }
        var selectedIndices by rememberRetained { mutableStateOf(setOf<Int>()) }
        var mergedSentence by rememberRetained { mutableStateOf("") }
        var isTextDetectionFailed by rememberRetained { mutableStateOf(false) }
        var isRecaptureDialogVisible by rememberRetained { mutableStateOf(false) }

        val analyzer = rememberRetained {
            liveTextAnalyzer.create(
                onTextDetected = { text ->
                    recognizedText = text
                },
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                analyzer.cancel()
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

                is OcrUiEvent.OnFrameReceived -> {
                    analyzer.analyze(event.imageProxy)
                }

                is OcrUiEvent.OnCaptureButtonClick -> {
                    if (recognizedText.isEmpty()) {
                        isTextDetectionFailed = true
                    } else {
                        isTextDetectionFailed = false

                        val sentences = recognizedText
                            .split("\n")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                        sentenceList = persistentListOf(*sentences.toTypedArray())

                        currentUi = OcrUi.RESULT
                    }
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
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OcrPresenter
    }
}
