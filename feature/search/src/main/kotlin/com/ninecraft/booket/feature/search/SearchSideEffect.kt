package com.ninecraft.booket.feature.search

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect
import com.orhanobut.logger.Logger

@Composable
internal fun HandleSearchSideEffects(
    state: SearchUiState,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is SearchSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
