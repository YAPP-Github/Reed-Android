package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.HomeRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val service: ReedService,
) : HomeRepository {
    override suspend fun getHome() = runSuspendCatching {
        service.getHome().toModel()
    }
}
