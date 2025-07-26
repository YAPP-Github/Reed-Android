package com.ninecraft.booket.feature.record.ocr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.ocr.analyzer.LiveTextAnalyzer
import com.ninecraft.booket.feature.screens.OcrScreen
import com.ninecraft.booket.feature.screens.RecordScreen
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
        var sentenceList by rememberRetained { mutableStateOf(emptyList<String>().toPersistentList()) }
        var recognizedText by rememberRetained { mutableStateOf("") }
        var selectedIndices by rememberRetained { mutableStateOf(setOf<Int>()) }
        var mergedSentence by rememberRetained { mutableStateOf("") }

        fun handleEvent(event: OcrUiEvent) {
            when (event) {
                is OcrUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                is OcrUiEvent.OnFrameReceived -> {
                    val analyzer = liveTextAnalyzer.create(
                        onTextDetected = { text ->
                            recognizedText = text
                        },
                        onFailure = {
                            // 실시간 인지는 프레임 단위로 실패 콜백을 전달하는데, UI 상태에 반영하기에는 애매함
                        },
                    )
                    analyzer.analyze(event.imageProxy)
                }

                is OcrUiEvent.OnCaptureButtonClick -> {
                    // 눌렀을 때, 해당 문자 수집
                    val sentences = recognizedText
                        .split("\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    sentenceList = persistentListOf(*sentences.toTypedArray())
                    currentUi = OcrUi.RESULT
                }

                is OcrUiEvent.OnReCaptureButtonClick -> {
                    currentUi = OcrUi.CAMERA
                }

                is OcrUiEvent.OnSelectionConfirmed -> {
                    mergedSentence = selectedIndices
                        .sorted().joinToString(" ") { sentenceList[it] }

                    navigator.goTo(RecordScreen(mergedSentence))
                }

                is OcrUiEvent.OnSentenceSelected -> {
                    selectedIndices = if (selectedIndices.contains(event.index)) {
                        selectedIndices - event.index
                    } else {
                        selectedIndices + event.index
                    }
                }
            }
        }

        return OcrUiState(
            currentUi = currentUi,
            sentenceList = sentenceList,
            selectedIndices = selectedIndices,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OcrPresenter
    }
}
