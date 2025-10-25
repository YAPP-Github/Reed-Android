package com.ninecraft.booket.feature.settings.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.NotificationScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class NotificationPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val userRepository: UserRepository,
) : Presenter<NotificationUiState> {
    @Composable
    override fun present(): NotificationUiState {
        val scope = rememberCoroutineScope()
        val isNotificationEnabled by userRepository.isNotificationEnabled.collectAsRetainedState(initial = false)

        fun updateNotificationSettings(enabled: Boolean) {
            scope.launch {
                userRepository.setNotificationEnabled(enabled)
                userRepository.updateNotificationSettings(enabled)
                    .onSuccess {
                        Logger.d("Notification settings updated successfully")
                    }
                    .onFailure { exception ->
                        Logger.d(exception.message.toString())
                    }
            }
        }

        fun handleEvent(event: NotificationUiEvent) {
            when (event) {
                is NotificationUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is NotificationUiEvent.OnNotificationToggle -> {
                    updateNotificationSettings(event.enabled)
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
