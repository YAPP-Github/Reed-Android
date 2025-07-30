package com.ninecraft.booket.feature.detail.book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.feature.screens.BookDetailScreen
import com.ninecraft.booket.feature.screens.LoginScreen
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

class BookDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: BookDetailScreen,
    @Assisted private val navigator: Navigator,
    private val repository: BookRepository,
) : Presenter<BookDetailUiState> {

    @Composable
    override fun present(): BookDetailUiState {
        val scope = rememberCoroutineScope()

        var sideEffect by rememberRetained { mutableStateOf<BookDetailSideEffect?>(null) }

        fun upsertBook(bookIsbn: String, bookStatus: String) {
            scope.launch {
                repository.upsertBook(bookIsbn, bookStatus)
                    .onSuccess {
                        sideEffect = BookDetailSideEffect.ShowToast("성공")
                    }
                    .onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = BookDetailSideEffect.ShowToast(message)
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

        fun handleEvent(event: BookDetailUiEvent) {
            when (event) {
                BookDetailUiEvent.InitSideEffect -> {
                    sideEffect = null
                }

                BookDetailUiEvent.OnBackClicked -> {
                    navigator.pop()
                }

                BookDetailUiEvent.OnBeforeReadingClick -> {
                    upsertBook(screen.isbn, "BEFORE_READING")
                }

                BookDetailUiEvent.OnReadingClick -> {
                    upsertBook(screen.isbn, "READING")
                }

                BookDetailUiEvent.OnCompletedClick -> {
                    upsertBook(screen.isbn, "COMPLETED")
                }
            }
        }

        return BookDetailUiState(
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(BookDetailScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            screen: BookDetailScreen,
            navigator: Navigator,
        ): BookDetailPresenter
    }
}
