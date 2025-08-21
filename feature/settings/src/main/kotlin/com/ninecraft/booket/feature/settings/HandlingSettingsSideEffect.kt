package com.ninecraft.booket.feature.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ninecraft.booket.core.common.extensions.openPlayStore
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleSettingsSideEffects(
    state: SettingsUiState,
    eventSink: (SettingsUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is SettingsSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is SettingsSideEffect.NavigateToPlayStore -> {
                context.openPlayStore()
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(SettingsUiEvent.InitSideEffect)
        }
    }
}
