package com.ninecraft.booket.feature.settings.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.NotificationScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
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
        val isNotificationEnabled by userRepository.isUserNotificationEnabled.collectAsRetainedState(initial = false)
        var sideEffect by rememberRetained { mutableStateOf<NotificationSideEffect?>(null) }

        fun updateNotificationSettings(enabled: Boolean) {
            scope.launch {
                val prevNotificationEnabled = userRepository.getUserNotificationEnabled()
                userRepository.setUserNotificationEnabled(enabled)

                userRepository.updateNotificationSettings(enabled)
                    .onSuccess {
                        userRepository.setLastNotificationSyncedEnabled(enabled)
                    }
                    .onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = NotificationSideEffect.ShowToast(message)
                        }

                        handleException(
                            exception = exception,
                            onError = handleErrorMessage,
                            onLoginRequired = {
                                navigator.resetRoot(LoginScreen())
                            },
                        )
                        userRepository.setUserNotificationEnabled(prevNotificationEnabled)
                    }
            }
        }

        suspend fun syncNotificationSettings(enabled: Boolean) {
            userRepository.updateNotificationSettings(enabled)
                .onSuccess {
                    userRepository.setLastNotificationSyncedEnabled(enabled)
                }
                .onFailure { exception ->
                    val handleErrorMessage = { message: String ->
                        Logger.e(message)
                        sideEffect = NotificationSideEffect.ShowToast(message)
                    }

                    handleException(
                        exception = exception,
                        onError = handleErrorMessage,
                        onLoginRequired = {
                            navigator.resetRoot(LoginScreen())
                        },
                    )
                }
        }

        fun handleEvent(event: NotificationUiEvent) {
            when (event) {
                is NotificationUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is NotificationUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is NotificationUiEvent.OnNotificationPermissionResult -> {
                    scope.launch {
                        val isPermissionGranted = event.granted
                        val userEnabled = userRepository.getUserNotificationEnabled()
                        val lastSyncedServerEnabled = userRepository.getLastSyncedNotificationEnabled()

                        val shouldSync = (!isPermissionGranted && lastSyncedServerEnabled != false) ||
                            (userEnabled && (lastSyncedServerEnabled == null || lastSyncedServerEnabled != isPermissionGranted))

                        if (shouldSync) {
                            syncNotificationSettings(isPermissionGranted)
                        }
                    }
                }

                is NotificationUiEvent.OnNotificationToggle -> {
                    updateNotificationSettings(event.enabled)
                }
            }
        }
        return NotificationUiState(
            isNotificationEnabled = isNotificationEnabled,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(NotificationScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): NotificationPresenter
    }
}
