package com.ninecraft.booket.core.data.impl.di

import com.ninecraft.booket.core.data.api.repository.AuthRepository
import com.ninecraft.booket.core.data.impl.repository.DefaultAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(defaultAuthRepository: DefaultAuthRepository): AuthRepository
}
