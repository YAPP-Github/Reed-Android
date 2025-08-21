package com.ninecraft.booket.core.data.impl.di

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

    @Binds
    @Singleton
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindBookRepository(defaultBookRepository: DefaultBookRepository): BookRepository

    @Binds
    @Singleton
    abstract fun bindRecordRepository(defaultRecordRepository: DefaultRecordRepository): RecordRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(defaultRemoteConfigRepository: DefaultRemoteConfigRepository): RemoteConfigRepository
}
