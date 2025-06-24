package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.feature.home.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

@Suppress("unused")
class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<LoginScreen.State> {

    @Composable
    override fun present(): LoginScreen.State {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LoginScreen.SideEffect?>(null) }

        fun showLoading() {
            isLoading = true
        }

        fun hideLoading() {
            isLoading = false
        }

        fun clearSideEffect() {
            sideEffect = null
        }

        fun handleEvent(event: LoginScreen.Event) {
            when (event) {
                is LoginScreen.Event.InitSideEffect -> clearSideEffect()
                is LoginScreen.Event.OnKakaoLoginButtonClick -> {
                    showLoading()
                    sideEffect = LoginScreen.SideEffect.KakaoLogin
                }

                is LoginScreen.Event.LoginFailure -> {
                    hideLoading()
                    sideEffect = LoginScreen.SideEffect.ShowToast(event.message)
                }

                is LoginScreen.Event.LoginSuccess -> {
                    hideLoading()
                    navigator.resetRoot(HomeScreen)
                }
            }
        }

        return LoginScreen.State(
            isLoading = isLoading,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}
