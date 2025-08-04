package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.HomeRepository
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SearchScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val repository: HomeRepository,
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        var sideEffect by rememberRetained { mutableStateOf<HomeSideEffect?>(null) }
        var recentBooks by rememberRetained { mutableStateOf(persistentListOf<RecentBookModel>()) }

        fun getHome() {
            scope.launch {
                repository.getHome()
                    .onSuccess { result ->
                        recentBooks = result.recentBooks.toPersistentList()
                    }.onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = HomeSideEffect.ShowToast(message)
                        }

                        handleException(
                            exception = exception,
                            onError = handleErrorMessage,
                            onLoginRequired = {
                                navigator.resetRoot(LoginScreen)
                            },
                        )
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
                    navigator.goTo(BookDetailScreen(""))
                }
            }
        }

        LaunchedEffect(true) {
            getHome()
        }

        return HomeUiState(
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
