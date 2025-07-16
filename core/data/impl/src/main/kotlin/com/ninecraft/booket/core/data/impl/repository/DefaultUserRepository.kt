package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.UserRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

internal class DefaultUserRepository @Inject constructor(
    private val service: ReedService,
) : UserRepository {
    override suspend fun getUserProfile() = runSuspendCatching {
        service.getUserProfile().toModel()
    }
}
