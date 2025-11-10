package com.ninecraft.booket.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.constants.WebViewConstants
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.model.UserState
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.NotificationScreen
import com.ninecraft.booket.feature.screens.OssLicensesScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.ninecraft.booket.feature.screens.WebViewScreen
import com.ninecraft.booket.feature.screens.extensions.redirectToLogin
import com.orhanobut.logger.Logger
import com.skydoves.compose.effects.RememberedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

@Inject
@CircuitInject(SettingsScreen::class, AppScope::class)
class SettingsPresenter(
    @Assisted val navigator: Navigator,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<SettingsUiState> {

    companion object {
        private const val SETTINGS_LOGOUT_COMPLETE = "settings_logout_complete"
        private const val SETTINGS_WITHDRAWAL_COMPLETE = "settings_withdrawal_complete"
        private const val SETTINGS_WITHDRAWAL_WARNING = "settings_withdrawal_warning"
    }

    @Composable
    override fun present(): SettingsUiState {
        val scope = rememberCoroutineScope()
        val userState by authRepository.userState.collectAsRetainedState(initial = UserState.Guest)
        var isLoading by rememberRetained { mutableStateOf(false) }
        var isLogoutDialogVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawConfirmed by rememberRetained { mutableStateOf(false) }
        var latestVersion by rememberRetained { mutableStateOf("") }
        var isOptionalUpdateDialogVisible by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<SettingsSideEffect?>(null) }

        fun logout() {
            scope.launch {
                try {
                    isLoading = true
                    authRepository.logout()
                        .onSuccess {
                            userRepository.resetNotificationData()
                            analyticsHelper.logEvent(SETTINGS_LOGOUT_COMPLETE)
                            navigator.resetRoot(LoginScreen())
                        }
                        .onFailure { exception ->
                            val handleErrorMessage = { message: String ->
                                Logger.e(message)
                                sideEffect = SettingsSideEffect.ShowToast(message)
                            }

                            handleException(
                                exception = exception,
                                onError = handleErrorMessage,
                                onLoginRequired = {
                                    navigator.resetRoot(LoginScreen())
                                },
                            )
                        }
                } finally {
                    isLoading = false
                }
            }
        }

        fun withdraw() {
            scope.launch {
                try {
                    isLoading = true
                    authRepository.withdraw()
                        .onSuccess {
                            userRepository.resetNotificationData()
                            analyticsHelper.logEvent(SETTINGS_WITHDRAWAL_COMPLETE)
                            navigator.resetRoot(LoginScreen())
                        }
                        .onFailure { exception ->
                            val handleErrorMessage = { message: String ->
                                Logger.e(message)
                                sideEffect = SettingsSideEffect.ShowToast(message)
                            }

                            handleException(
                                exception = exception,
                                onError = handleErrorMessage,
                                onLoginRequired = {
                                    navigator.resetRoot(LoginScreen())
                                },
                            )
                        }
                } finally {
                    isLoading = false
                }
            }
        }

        fun getLatestVersion() {
            scope.launch {
                try {
                    isLoading = true
                    remoteConfigRepository.getLatestVersion()
                        .onSuccess { version ->
                            latestVersion = version
                        }
                        .onFailure { exception ->
                            val handleErrorMessage = { message: String ->
                                Logger.e(message)
                                sideEffect = SettingsSideEffect.ShowToast(message)
                            }

                            handleException(
                                exception = exception,
                                onError = handleErrorMessage,
                            )
                        }
                } finally {
                    isLoading = false
                }
            }
        }

        fun handleEvent(event: SettingsUiEvent) {
            when (event) {
                is SettingsUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                is SettingsUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is SettingsUiEvent.OnPolicyClick -> {
                    val policy = WebViewConstants.PRIVACY_POLICY
                    navigator.goTo(WebViewScreen(url = policy.url, title = policy.title))
                }

                is SettingsUiEvent.OnNotificationClick -> {
                    navigator.goTo(NotificationScreen)
                }

                is SettingsUiEvent.OnTermClick -> {
                    val terms = WebViewConstants.TERMS_OF_SERVICE
                    navigator.goTo(WebViewScreen(url = terms.url, title = terms.title))
                }

                is SettingsUiEvent.OnOssLicensesClick -> {
                    navigator.goTo(OssLicensesScreen)
                }

                is SettingsUiEvent.OnLogoutClick -> {
                    isLogoutDialogVisible = true
                }

                is SettingsUiEvent.OnWithdrawClick -> {
                    analyticsHelper.logEvent(SETTINGS_WITHDRAWAL_WARNING)
                    isWithdrawBottomSheetVisible = true
                }

                is SettingsUiEvent.OnBottomSheetDismissed -> {
                    isLogoutDialogVisible = false
                    isWithdrawBottomSheetVisible = false
                    isWithdrawConfirmed = false
                }

                is SettingsUiEvent.OnWithdrawConfirmationToggled -> {
                    isWithdrawConfirmed = !isWithdrawConfirmed
                }

                is SettingsUiEvent.Logout -> {
                    isLogoutDialogVisible = false
                    logout()
                }

                is SettingsUiEvent.Withdraw -> {
                    isWithdrawBottomSheetVisible = false
                    withdraw()
                }

                is SettingsUiEvent.OnVersionClick -> {
                    sideEffect = SettingsSideEffect.NavigateToPlayStore
                }

                is SettingsUiEvent.OnLoginClick -> {
                    scope.launch {
                        navigator.redirectToLogin()
                    }
                }
            }
        }

        RememberedEffect(Unit) {
            getLatestVersion()
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(SettingsScreen.name)
        }

        return SettingsUiState(
            isLoading = isLoading,
            isLogoutDialogVisible = isLogoutDialogVisible,
            isWithdrawBottomSheetVisible = isWithdrawBottomSheetVisible,
            isWithdrawConfirmed = isWithdrawConfirmed,
            latestVersion = latestVersion,
            isOptionalUpdateDialogVisible = isOptionalUpdateDialogVisible,
            isGuestMode = userState is UserState.Guest,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}
