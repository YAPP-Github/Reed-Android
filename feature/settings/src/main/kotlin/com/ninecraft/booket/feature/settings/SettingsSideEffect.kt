package com.ninecraft.booket.feature.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun HandleSettingsSideEffects(
    state: SettingsUiState,
    eventSink: (SettingsUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state) {
        when (state.sideEffect) {
            is SettingsSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(SettingsUiEvent.InitSideEffect)
        }
    }
}
