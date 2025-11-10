package com.ninecraft.booket.di

import android.app.Application
import android.content.Context
import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.data.api.repository.RemoteConfigRepository
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultAuthRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultBookRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultRecordRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultRemoteConfigRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultUserRepository
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AppGraph {

    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @Multibinds(allowEmpty = true)
    val presenterFactories: Set<Presenter.Factory>

    @Multibinds(allowEmpty = true)
    val uiFactories: Set<Ui.Factory>

    @Provides
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit {
        return Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .setAnimatedNavDecoratorFactory(CrossFadeNavDecoratorFactory())
            .build()
    }
    
    @Provides
    fun provideAuthRepository(impl: DefaultAuthRepository): AuthRepository = impl

    @Provides
    fun provideUserRepository(impl: DefaultUserRepository): UserRepository = impl

    @Provides
    fun provideBookRepository(impl: DefaultBookRepository): BookRepository = impl

    @Provides
    fun provideRecordRepository(impl: DefaultRecordRepository): RecordRepository = impl

    @Provides
    fun provideRemoteConfigRepository(impl: DefaultRemoteConfigRepository): RemoteConfigRepository = impl

    val circuit: Circuit

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
