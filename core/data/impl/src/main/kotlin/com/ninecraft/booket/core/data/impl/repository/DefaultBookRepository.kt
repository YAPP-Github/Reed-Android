package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.service.NoAuthService
import javax.inject.Inject

internal class DefaultBookRepository @Inject constructor(
    private val service: NoAuthService,
) : BookRepository {
    override suspend fun searchBook(
        query: String,
        start: Int,
    ) = runSuspendCatching {
        service.searchBook(
            query = query,
            start = start,
        ).toModel()
    }

    override suspend fun getBookDetail(itemId: String) = runSuspendCatching {
        service.getBookDetail(itemId).toModel()
    }
}
