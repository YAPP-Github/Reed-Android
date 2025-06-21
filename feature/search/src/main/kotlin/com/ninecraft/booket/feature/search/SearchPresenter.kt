package com.ninecraft.booket.feature.search

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
class SearchPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<SearchScreen.State> {

    @Composable
    override fun present(): SearchScreen.State {
        val scope = rememberCoroutineScope()

        return SearchScreen.State {}
    }

    @CircuitInject(SearchScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SearchPresenter
    }
}
