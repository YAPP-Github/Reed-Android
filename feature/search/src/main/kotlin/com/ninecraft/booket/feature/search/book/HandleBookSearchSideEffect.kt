package com.ninecraft.booket.feature.search.book

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleBookSearchSideEffects(
    state: BookSearchUiState,
    eventSink: (BookSearchUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is BookSearchSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(BookSearchUiEvent.InitSideEffect)
        }
    }
}
