package com.ninecraft.booket.feature.record.ocr

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleOcrSideEffects(
    state: OcrUiState,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is OcrSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
