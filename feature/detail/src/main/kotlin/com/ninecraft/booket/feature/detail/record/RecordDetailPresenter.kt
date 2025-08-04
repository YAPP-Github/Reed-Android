package com.ninecraft.booket.feature.detail.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.model.RecordDetailModel
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.RecordDetailScreen
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

class RecordDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: RecordDetailScreen,
    @Assisted private val navigator: Navigator,
    private val repository: RecordRepository,
) : Presenter<RecordDetailUiState> {

    @Composable
    override fun present(): RecordDetailUiState {
        val scope = rememberCoroutineScope()

        var recordDetailInfo by rememberRetained { mutableStateOf(RecordDetailModel()) }
        var sideEffect by rememberRetained { mutableStateOf<RecordDetailSideEffect?>(null) }

        fun getRecordDetail(readingRecordId: String) {
            scope.launch {
                repository.getRecordDetail(readingRecordId = readingRecordId)
                    .onSuccess { result ->
                        recordDetailInfo = result
                    }
                    .onFailure { exception ->
                        val handleErrorMessage = { message: String ->
                            Logger.e(message)
                            sideEffect = RecordDetailSideEffect.ShowToast(message)
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

        fun handleEvent(event: RecordDetailUiEvent) {
            when (event) {
                RecordDetailUiEvent.OnCloseClicked -> {
                    navigator.pop()
                }
            }
        }

        LaunchedEffect(Unit) {
            getRecordDetail(screen.recordId)
        }

        return RecordDetailUiState(
            recordDetailInfo = recordDetailInfo,
            sideEffect = sideEffect,
            eventSink = ::handleEvent,
        )
    }
}

@CircuitInject(RecordDetailScreen::class, ActivityRetainedComponent::class)
@AssistedFactory
fun interface Factory {
    fun create(
        screen: RecordDetailScreen,
        navigator: Navigator,
    ): RecordDetailPresenter
}
