package com.ninecraft.booket.feature.settings.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.screens.NotificationScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class NotificationPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
) : Presenter<NotificationUiState> {
    @Composable
    override fun present(): NotificationUiState {
        var isNotificationEnabled by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: NotificationUiEvent) {
            when (event) {
                is NotificationUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is NotificationUiEvent.OnNotificationToggle -> {
                    isNotificationEnabled = !isNotificationEnabled
                }
            }
        }
        return NotificationUiState(
            isNotificationEnabled = isNotificationEnabled,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(NotificationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): NotificationPresenter
    }
}
