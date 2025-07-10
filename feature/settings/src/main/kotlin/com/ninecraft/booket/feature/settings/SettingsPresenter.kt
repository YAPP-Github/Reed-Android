package com.ninecraft.booket.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class SettingsPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
) : Presenter<SettingsScreen.State> {

    @Composable
    override fun present(): SettingsScreen.State {
        var isLogoutSheetVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawSheetVisible by rememberRetained { mutableStateOf(false) }
        var isWithdrawConfirmed by rememberRetained { mutableStateOf(false) }

        fun handleEvent(event: SettingsScreen.Event) {
            when (event) {
                is SettingsScreen.Event.OnBackClick -> {
                    navigator.pop()
                }

                is SettingsScreen.Event.OnTermDetailClick -> {
                    // TODO: 웹뷰 화면으로 이동
                }

                is SettingsScreen.Event.OnLogoutClick -> {
                    isLogoutSheetVisible = true
                }

                is SettingsScreen.Event.OnWithdrawClick -> {
                    isWithdrawSheetVisible = true
                }

                is SettingsScreen.Event.OnBottomSheetDismissed -> {
                    isLogoutSheetVisible = false
                    isWithdrawSheetVisible = false
                }

                is SettingsScreen.Event.OnWithdrawConfirmationToggled -> {
                    isWithdrawConfirmed = !isWithdrawConfirmed
                }

                is SettingsScreen.Event.Logout -> {
                    // TODO: 로그아웃 처리 -> 성공 시 로그인 화면으로 이동
                }

                is SettingsScreen.Event.Withdraw -> {
                    // TODO: 회원탈퇴 처리 -> 성공 시 로그인 화면으로 이동
                }
            }
        }
        return SettingsScreen.State(
            isLogoutSheetVisible = isLogoutSheetVisible,
            isWithdrawSheetVisible = isWithdrawSheetVisible,
            isWithdrawConfirmed = isWithdrawConfirmed,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(SettingsScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPresenter
    }
}
