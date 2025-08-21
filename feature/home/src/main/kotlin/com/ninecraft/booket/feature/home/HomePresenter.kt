package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SearchScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.skydoves.compose.effects.RememberedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.ImpressionEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()

        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var recentBooks by rememberRetained { mutableStateOf(persistentListOf<RecentBookModel>()) }

        fun loadHomeContent() {
            scope.launch {
                if (uiState is UiState.Idle || uiState is UiState.Error) {
                    uiState = UiState.Loading
                }

                repository.getHome()
                    .onSuccess { result ->
                        uiState = UiState.Success
                        recentBooks = result.recentBooks.toPersistentList()
                    }.onFailure { exception ->
                        uiState = UiState.Error(exception)
                    }
            }
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnSettingsClick -> {
                    navigator.goTo(SettingsScreen)
                }

                is HomeUiEvent.OnBookRegisterClick -> {
                    navigator.goTo(SearchScreen)
                }

                is HomeUiEvent.OnRecordButtonClick -> {
                    navigator.goTo(RecordScreen(event.userBookId))
                }

                is HomeUiEvent.OnBookDetailClick -> {
                    navigator.goTo(BookDetailScreen(event.userBookId, event.isbn13))
                }

                is HomeUiEvent.OnRetryClick -> {
                    loadHomeContent()
                }

                is HomeUiEvent.OnTabSelected -> {
                    navigator.resetRoot(
                        newRoot = event.tab.screen,
                        saveState = true,
                        restoreState = true,
                    )
                }
            }
        }

        RememberedEffect(true) {
            loadHomeContent()
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(HomeScreen.name)
        }

        return HomeUiState(
            uiState = uiState,
            recentBooks = recentBooks,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
