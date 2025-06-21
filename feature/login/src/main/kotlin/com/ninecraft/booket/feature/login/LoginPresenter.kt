package com.ninecraft.booket.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
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

        return LoginScreen.State {}
    }

    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}
