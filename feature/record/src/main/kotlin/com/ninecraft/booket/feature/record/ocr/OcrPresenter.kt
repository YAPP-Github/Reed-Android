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
import kotlinx.collections.immutable.persistentListOf

class OcrPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val liveTextAnalyzer: LiveTextAnalyzer.Factory,
) : Presenter<OcrUiState> {

    @Composable
    override fun present(): OcrUiState {
        var currentUi by rememberRetained { mutableStateOf(OcrUi.CAMERA) }
        var sentenceList by rememberRetained {
            mutableStateOf(
                persistentListOf(
                    "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                    "소설가들은 늘 소재를 찾아 떠도는 존재 같지만, 실은 그 반대인 경우가 더 잦다.",
                ),
            )
        }
        var recognizedText by rememberRetained { mutableStateOf("") }

        fun handleEvent(event: OcrUiEvent) {
            when (event) {
                is OcrUiEvent.OnCloseClick -> {
                    navigator.pop()
                }

                is OcrUiEvent.OnImageCaptured -> {
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

                is OcrUiEvent.OnCapture -> {
                    // 눌렀을 때, 해당 문자 수집
                    val sentences = recognizedText
                        .split("\n")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    sentenceList = persistentListOf(*sentences.toTypedArray())
                    currentUi = OcrUi.RESULT
                }

                is OcrUiEvent.OnReCapture -> {
                    currentUi = OcrUi.CAMERA
                }
            }
        }

        return OcrUiState(
            currentUi = currentUi,
            sentenceList = sentenceList,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): OcrPresenter
    }
}
