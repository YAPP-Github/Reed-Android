package com.ninecraft.booket.feature.library

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleLibrarySideEffects(
    state: LibraryUiState,
    eventSink: (LibraryUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is LibrarySideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(LibraryUiEvent.InitSideEffect)
        }
    }
}
