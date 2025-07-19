package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.datastore.api.datasource.RecentSearchDataSource
import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

internal class DefaultBookRepository @Inject constructor(
    private val service: ReedService,
    private val dataSource: RecentSearchDataSource,
) : BookRepository {
    override suspend fun searchBook(
        query: String,
        start: Int,
    ) = runSuspendCatching {
        val result = service.searchBook(
            query = query,
            start = start,
        ).toModel()

        dataSource.addRecentSearch(query)
        result
    }

    override suspend fun getBookDetail(itemId: String) = runSuspendCatching {
        service.getBookDetail(itemId).toModel()
    }

    override suspend fun upsertBook(bookIsbn: String, bookStatus: String) = runSuspendCatching {
        service.upsertBook(BookUpsertRequest(bookIsbn, bookStatus)).toModel()
    }
}
