package com.ninecraft.booket.feature.record.ocr

import androidx.compose.runtime.Composable
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
import kotlinx.collections.immutable.toPersistentList

class OcrPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val liveTextAnalyzer: LiveTextAnalyzer.Factory,
) : Presenter<OcrUiState> {


    @Composable
    override fun present(): OcrUiState {
        var currentUi by rememberRetained { mutableStateOf(OcrUi.CAMERA) }
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

        fun handleEvent(event: OcrUiEvent) {
            when (event) {
                is OcrUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                is OcrUiEvent.OnFrameReceived -> {
                    analyzer.analyze(event.imageProxy)
                }

                is OcrUiEvent.OnCaptureButtonClick -> {
                    if (recognizedText.isEmpty()) {
                        isTextDetectionFailed = true
                    } else {
                        isTextDetectionFailed = false

                        val parsedSentences = parseSentences(recognizedText)
                        sentenceList = parsedSentences.toPersistentList()

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

fun parseSentences(text: String): List<String> {
    val containsPunctuation = text.contains(Regex("[.!?]"))

    return if (containsPunctuation) {
        text.replace("\n", " ")
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    } else {
        text.split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}
