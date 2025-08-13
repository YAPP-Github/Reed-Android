package com.ninecraft.booket.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.constants.WebViewConstants
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.OssLicensesScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.ninecraft.booket.feature.screens.WebViewScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class SettingsPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    private val authRepository: AuthRepository,
) : Presenter<SettingsUiState> {

    @Composable
    override fun present(): SettingsUiState {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<SettingsSideEffect?>(null) }
        var isLogoutDialogVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawConfirmed by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: SettingsUiEvent) {
            when (event) {
                is SettingsUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is SettingsUiEvent.OnPolicyClick -> {
                    val policy = WebViewConstants.PRIVACY_POLICY
                    navigator.goTo(WebViewScreen(url = policy.url, title = policy.title))
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
                    scope.launch {
                        try {
                            isLoading = true
                            authRepository.logout()
                                .onSuccess {
                                    navigator.resetRoot(LoginScreen)
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
                                            navigator.resetRoot(LoginScreen)
                                        },
                                    )
                                }
                        } finally {
                            isLoading = false
                        }
                    }
                    isLogoutDialogVisible = false
                }

                is SettingsUiEvent.Withdraw -> {
                    scope.launch {
                        try {
                            isLoading = true
                            authRepository.withdraw()
                                .onSuccess {
                                    navigator.resetRoot(LoginScreen)
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
                                            navigator.resetRoot(LoginScreen)
                                        },
                                    )
                                }
                        } finally {
                            isLoading = false
                        }
                    }
                    isWithdrawBottomSheetVisible = false
                }
            }
        }
        return SettingsUiState(
            isLoading = isLoading,
            isLogoutDialogVisible = isLogoutDialogVisible,
            isWithdrawBottomSheetVisible = isWithdrawBottomSheetVisible,
            isWithdrawConfirmed = isWithdrawConfirmed,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPresenter
    }
}
