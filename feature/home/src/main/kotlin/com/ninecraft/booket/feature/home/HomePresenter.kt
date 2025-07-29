package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SearchScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
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

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnSettingsClick -> {
                    navigator.goTo(SettingsScreen)
                }

                is HomeUiEvent.OnBookRegisterClick -> {
                    navigator.goTo(SearchScreen)
                }

                is HomeUiEvent.OnRecordButtonClick -> {
                    navigator.goTo(RecordScreen)
                }

                is HomeUiEvent.OnBookDetailClick -> {
                    navigator.goTo(BookDetailScreen(""))
                }
            }
        }

        return HomeUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
