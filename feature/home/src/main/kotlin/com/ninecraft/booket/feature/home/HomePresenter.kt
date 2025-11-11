package com.ninecraft.booket.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.analytics.AnalyticsHelper
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.core.model.UserState
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.BookSearchScreen
import com.ninecraft.booket.feature.screens.HomeScreen
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordScreen
import com.ninecraft.booket.feature.screens.SettingsScreen
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
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@AssistedInject
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val bookRepository: BookRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val analyticsHelper: AnalyticsHelper,
) : Presenter<HomeUiState> {

    @CircuitInject(HomeScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }

    @Composable
    override fun present(): HomeUiState {
        val scope = rememberCoroutineScope()
        val userState by authRepository.userState.collectAsRetainedState(initial = UserState.Guest)
        var uiState by rememberRetained { mutableStateOf<UiState>(UiState.Idle) }
        var recentBooks by rememberRetained { mutableStateOf(persistentListOf<RecentBookModel>()) }

        fun loadHomeContent() {
            scope.launch {
                if (uiState is UiState.Idle || uiState is UiState.Error) {
                    uiState = UiState.Loading
                }

                bookRepository.getHome()
                    .onSuccess { result ->
                        uiState = UiState.Success
                        recentBooks = result.recentBooks.toPersistentList()
                    }.onFailure { exception ->
                        uiState = UiState.Error(exception)

                        handleException(
                            exception = exception,
                            onError = {},
                            onLoginRequired = {
                                navigator.resetRoot(LoginScreen())
                            },
                        )
                    }
            }
        }

        suspend fun syncNotificationSettings(isGranted: Boolean) {
            userRepository.updateNotificationSettings(isGranted)
                .onSuccess {
                    userRepository.setLastNotificationSyncedEnabled(isGranted)
                }.onFailure { exception ->
                    Logger.e("Failed to update notification settings: $exception")
                }
        }

        fun handleEvent(event: HomeUiEvent) {
            when (event) {
                is HomeUiEvent.OnSettingsClick -> {
                    navigator.goTo(SettingsScreen)
                }

                is HomeUiEvent.OnBookRegisterClick -> {
                    navigator.goTo(BookSearchScreen)
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

                is HomeUiEvent.OnNotificationPermissionResult -> {
                    scope.launch {
                        val isPermissionGranted = event.granted
                        val userEnabled = userRepository.getUserNotificationEnabled()
                        val lastSyncedServerEnabled = userRepository.getLastSyncedNotificationEnabled()

                        val shouldSync = (!isPermissionGranted && lastSyncedServerEnabled != false) ||
                            (userEnabled && (lastSyncedServerEnabled == null || lastSyncedServerEnabled != isPermissionGranted))

                        if (shouldSync) {
                            syncNotificationSettings(isPermissionGranted)
                        }
                    }
                }
            }
        }

        RememberedEffect(userState) {
            if (userState !is UserState.Guest) {
                loadHomeContent()
            }
        }

        ImpressionEffect {
            analyticsHelper.logScreenView(HomeScreen.name)
        }

        return HomeUiState(
            uiState = uiState,
            recentBooks = recentBooks,
            isGuestMode = userState is UserState.Guest,
            eventSink = ::handleEvent,
        )
    }
}
