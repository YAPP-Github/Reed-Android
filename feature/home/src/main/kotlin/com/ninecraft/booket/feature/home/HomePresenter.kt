package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.screens.HomeScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

@Suppress("unused")
class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()

        return HomeUiState {}
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
