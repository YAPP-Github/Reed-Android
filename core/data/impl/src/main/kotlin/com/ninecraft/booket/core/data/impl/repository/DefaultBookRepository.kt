package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

internal class DefaultBookRepository @Inject constructor(
    private val service: ReedService,
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

    override suspend fun upsertBook(bookIsbn: String, bookStatus: String) = runSuspendCatching {
        service.upsertBook(BookUpsertRequest(bookIsbn, bookStatus)).toModel()
    }
}
