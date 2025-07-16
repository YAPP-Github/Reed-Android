package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.service.AuthService
import javax.inject.Inject

internal class DefaultBookRepository @Inject constructor(
    private val authService: AuthService,
) : BookRepository {
    override suspend fun searchBook(
        query: String,
        start: Int,
    ) = runSuspendCatching {
        authService.searchBook(
            query = query,
            start = start,
        ).toModel()
    }

    override suspend fun getBookDetail(itemId: String) = runSuspendCatching {
        authService.getBookDetail(itemId).toModel()
    }

    override suspend fun upsertBook(bookIsbn: String, bookStatus: String) = runSuspendCatching {
        authService.upsertBook(BookUpsertRequest(bookIsbn, bookStatus)).toModel()
    }
}
