package com.ninecraft.booket.feature.record.ocr

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ninecraft.booket.feature.record.ocr.content.OcrCameraContent
import com.ninecraft.booket.feature.record.ocr.content.OcrImageContent
import com.ninecraft.booket.feature.record.ocr.content.OcrResultContent
import com.ninecraft.booket.feature.screens.OcrScreen
import com.skydoves.compose.stability.runtime.TraceRecomposition
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@TraceRecomposition
@CircuitInject(OcrScreen::class, AppScope::class)
@Composable
internal fun OcrUi(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    HandleOcrSideEffects(state = state)

    when (state.currentUi) {
        OcrUi.CAMERA -> OcrCameraContent(state = state, modifier = modifier)
        OcrUi.IMAGE -> OcrImageContent(state = state, modifier = modifier)
        OcrUi.RESULT -> OcrResultContent(state = state, modifier = modifier)
    }
}
