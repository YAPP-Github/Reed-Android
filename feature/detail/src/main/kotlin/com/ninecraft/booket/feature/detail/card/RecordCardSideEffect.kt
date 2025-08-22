package com.ninecraft.booket.feature.detail.card

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.ninecraft.booket.core.common.extensions.externalShareForBitmap
import com.ninecraft.booket.core.common.extensions.saveImageToGallery
import com.ninecraft.booket.feature.detail.R
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleRecordCardSideEffects(
    state: RecordCardUiState,
    recordCardGraphicsLayer: GraphicsLayer,
    eventSink: (RecordCardUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is RecordCardSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is RecordCardSideEffect.SaveImage -> {
                context.saveImageToGallery(state.sideEffect.bitmap)
                Toast.makeText(context, context.getString(R.string.save_image_complete), Toast.LENGTH_SHORT).show()
            }

            is RecordCardSideEffect.ShareImage -> {
                context.externalShareForBitmap(state.sideEffect.bitmap)
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(RecordCardUiEvent.InitSideEffect)
        }
    }

    LaunchedEffect(state.isCapturing) {
        if (state.isCapturing) {
            eventSink(RecordCardUiEvent.SaveRecordCard(recordCardGraphicsLayer.toImageBitmap()))
        }
    }

    LaunchedEffect(state.isSharing) {
        if (state.isSharing) {
            eventSink(RecordCardUiEvent.ShareRecordCard(recordCardGraphicsLayer.toImageBitmap()))
        }
    }
}
