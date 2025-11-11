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
import com.ninecraft.booket.core.di.DataScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface DataGraph {

    @Binds
    val DefaultAuthRepository.bindAuthRepository: AuthRepository

    @Binds
    val DefaultBookRepository.bindBookRepository: BookRepository

    @Binds
    val DefaultRecordRepository.bindRecordRepository: RecordRepository

    @Binds
    val DefaultRemoteConfigRepository.bindRemoteConfigRepository: RemoteConfigRepository

    @Binds
    val DefaultUserRepository.bindUserRepository: UserRepository
}