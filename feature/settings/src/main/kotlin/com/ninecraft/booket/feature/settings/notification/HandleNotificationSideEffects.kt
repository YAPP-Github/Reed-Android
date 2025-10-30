package com.ninecraft.booket.feature.settings.notification

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleNotificationSideEffects(
    state: NotificationUiState,
    eventSink: (NotificationUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is NotificationSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(NotificationUiEvent.InitSideEffect)
        }
    }
}
