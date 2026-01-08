package com.ninecraft.booket.feature.edit.emotion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ninecraft.booket.core.common.utils.handleException
import com.ninecraft.booket.core.data.api.repository.EmotionRepository
import com.ninecraft.booket.core.model.EmotionCode
import com.ninecraft.booket.core.model.EmotionGroupModel
import com.ninecraft.booket.feature.screens.EmotionEditScreen
import com.ninecraft.booket.feature.screens.EmotionEditScreen.Result
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.arguments.DetailEmotionArg
import com.ninecraft.booket.feature.screens.arguments.PrimaryEmotionArg
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@AssistedInject
class EmotionEditPresenter(
    @Assisted private val screen: EmotionEditScreen,
    @Assisted private val navigator: Navigator,
    private val emotionRepository: EmotionRepository,
) : Presenter<EmotionEditUiState> {

    @CircuitInject(EmotionEditScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: EmotionEditScreen, navigator: Navigator): EmotionEditPresenter
    }

    @Composable
    override fun present(): EmotionEditUiState {
        val scope = rememberCoroutineScope()
        var emotionGroups by rememberRetained { mutableStateOf(persistentListOf<EmotionGroupModel>()) }
        var selectedEmotionCode by rememberRetained { mutableStateOf<EmotionCode?>(null) }
        var selectedEmotionMap by rememberRetained { mutableStateOf<Map<EmotionCode, ImmutableList<String>>>(emptyMap()) }
        var committedEmotionCode by rememberRetained { mutableStateOf<EmotionCode?>(null) }
        var committedEmotionMap by rememberRetained { mutableStateOf<Map<EmotionCode, ImmutableList<String>>>(emptyMap()) }
        var isEmotionDetailBottomSheetVisible by rememberRetained { mutableStateOf(false) }
        val isEditButtonEnabled by remember {
            derivedStateOf {
                val originalEmotionCode = screen.primaryEmotionCode
                val originalDetailIds = screen.detailEmotionIds.toSet()

                val currentEmotionCode = committedEmotionCode
                val currentDetailIds = committedEmotionMap[currentEmotionCode].orEmpty().toSet()

                val isPrimaryEmotionChanged = originalEmotionCode != currentEmotionCode
                val isDetailEmotionChanged = originalDetailIds != currentDetailIds

                isPrimaryEmotionChanged || isDetailEmotionChanged
            }
        }

        fun handleEvent(event: EmotionEditUiEvent) {
            when (event) {
                is EmotionEditUiEvent.OnBackClick -> {
                    navigator.pop()
                }

                is EmotionEditUiEvent.OnEditButtonClick -> {
                    val committedCode = committedEmotionCode ?: EmotionCode.OTHER
                    val committedDetailIds = committedEmotionMap[committedCode].orEmpty()

                    val primaryEmotionArg = emotionGroups.firstOrNull { it.code == committedCode }
                        ?.let {
                            PrimaryEmotionArg(
                                code = it.code,
                                displayName = it.displayName,
                            )
                        }
                        ?: PrimaryEmotionArg(
                            code = EmotionCode.OTHER,
                            displayName = "기타",
                        )

                    val detailEmotionArgs =
                        emotionGroups
                            .firstOrNull { it.code == committedCode }
                            ?.detailEmotions
                            ?.filter { it.id in committedDetailIds }
                            ?.map {
                                DetailEmotionArg(
                                    id = it.id,
                                    name = it.name,
                                )
                            }
                            .orEmpty()

                    navigator.pop(
                        result = Result(
                            primaryEmotion = primaryEmotionArg,
                            detailEmotions = detailEmotionArgs,
                        ),
                    )
                }

                is EmotionEditUiEvent.OnSelectEmotionCode -> {
                    selectedEmotionCode = event.emotionCode

                    if (selectedEmotionCode == EmotionCode.OTHER) {
                        committedEmotionCode = selectedEmotionCode
                    } else {
                        isEmotionDetailBottomSheetVisible = true
                    }
                }

                is EmotionEditUiEvent.OnEmotionDetailToggled -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val currentDetails = selectedEmotionMap[selectedEmotionCode].orEmpty()
                    val updatedDetails = if (event.detailId in currentDetails) {
                        currentDetails - event.detailId
                    } else {
                        currentDetails + event.detailId
                    }

                    selectedEmotionMap = selectedEmotionMap + (emotionKey to updatedDetails.toPersistentList())
                }

                is EmotionEditUiEvent.OnEmotionDetailRemoved -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val currentDetails = committedEmotionMap[selectedEmotionCode].orEmpty()
                    val updatedDetails = currentDetails - event.detailId

                    committedEmotionMap = committedEmotionMap + (emotionKey to updatedDetails.toPersistentList())
                    selectedEmotionMap = selectedEmotionMap + (emotionKey to updatedDetails.toPersistentList())
                }

                is EmotionEditUiEvent.OnEmotionDetailCommitted -> {
                    val emotionKey = selectedEmotionCode ?: return
                    val details = selectedEmotionMap[emotionKey] ?: persistentListOf()

                    committedEmotionCode = emotionKey
                    committedEmotionMap = mapOf(emotionKey to details)
                    selectedEmotionMap = mapOf(emotionKey to details)
                    isEmotionDetailBottomSheetVisible = false
                }

                is EmotionEditUiEvent.OnEmotionDetailSkipped -> {
                    committedEmotionCode = selectedEmotionCode
                    // 건너뛰기 시 세부감정 선택 초기화
                    committedEmotionMap = persistentMapOf()
                    selectedEmotionMap = persistentMapOf()
                    isEmotionDetailBottomSheetVisible = false
                }

                is EmotionEditUiEvent.OnEmotionDetailBottomSheetDismiss -> {
                    isEmotionDetailBottomSheetVisible = false
                }
            }
        }

        fun getEmotionGroups() {
            scope.launch {
                emotionRepository.getEmotions()
                    .onSuccess { result ->
                        emotionGroups = result.emotions.toPersistentList()
                        selectedEmotionCode = screen.primaryEmotionCode
                        selectedEmotionMap = mapOf(screen.primaryEmotionCode to screen.detailEmotionIds.toPersistentList())
                        committedEmotionCode = screen.primaryEmotionCode
                        committedEmotionMap = mapOf(screen.primaryEmotionCode to screen.detailEmotionIds.toPersistentList())
                    }.onFailure { exception ->
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

        LaunchedEffect(Unit) {
            getEmotionGroups()
        }

        return EmotionEditUiState(
            emotionGroups = emotionGroups,
            selectedEmotionCode = selectedEmotionCode,
            selectedEmotionMap = selectedEmotionMap,
            committedEmotion = committedEmotionCode,
            committedEmotionMap = committedEmotionMap,
            isEmotionDetailBottomSheetVisible = isEmotionDetailBottomSheetVisible,
            isEditButtonEnabled = isEditButtonEnabled,
            eventSink = ::handleEvent,
        )
    }
}
