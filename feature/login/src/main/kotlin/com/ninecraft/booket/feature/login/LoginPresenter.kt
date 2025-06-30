package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.feature.home.HomeScreen
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

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: AuthRepository,
) : Presenter<LoginScreen.State> {

    @Composable
    override fun present(): LoginScreen.State {
        val scope = rememberCoroutineScope()
        var isLoading by rememberRetained { mutableStateOf(false) }
        var sideEffect by rememberRetained { mutableStateOf<LoginScreen.SideEffect?>(null) }

        fun handleEvent(event: LoginScreen.Event) {
            when (event) {
                is LoginScreen.Event.InitSideEffect -> {
                    sideEffect = null
                }

                is LoginScreen.Event.OnKakaoLoginButtonClick -> {
                    isLoading = true
                    sideEffect = LoginScreen.SideEffect.KakaoLogin
                }

                is LoginScreen.Event.LoginFailure -> {
                    isLoading = false
                    sideEffect = LoginScreen.SideEffect.ShowToast(event.message)
                }

                is LoginScreen.Event.Login -> {
                    scope.launch {
                        repository.login(event.accessToken)
                            .onSuccess {
                                // TODO Token 저장
                                navigator.resetRoot(HomeScreen)
                            }.onFailure { exception ->
                                exception.message?.let { Logger.e(it) }
                                sideEffect = exception.message?.let {
                                    LoginScreen.SideEffect.ShowToast(it)
                                }
                            }
                        isLoading = false
                    }
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
