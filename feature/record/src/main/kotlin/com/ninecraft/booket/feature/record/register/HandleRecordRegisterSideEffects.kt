package com.ninecraft.booket.feature.record.register

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleRecordRegisterSideEffects(
    state: RecordRegisterUiState,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is RecordRegisterSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
