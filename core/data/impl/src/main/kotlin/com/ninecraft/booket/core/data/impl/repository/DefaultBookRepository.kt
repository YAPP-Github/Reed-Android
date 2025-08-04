package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.BookRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.datastore.api.datasource.BookRecentSearchDataSource
import com.ninecraft.booket.core.datastore.api.datasource.LibraryRecentSearchDataSource
import com.ninecraft.booket.core.network.request.BookUpsertRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

internal class DefaultBookRepository @Inject constructor(
    private val service: ReedService,
    private val bookRecentSearchDataSource: BookRecentSearchDataSource,
    private val libraryRecentSearchDataSource: LibraryRecentSearchDataSource,
) : BookRepository {
    override val bookRecentSearches = bookRecentSearchDataSource.recentSearches
    override val libraryRecentSearches = libraryRecentSearchDataSource.recentSearches

    override suspend fun searchBook(
        query: String,
        start: Int,
    ) = runSuspendCatching {
        val result = service.searchBook(
            query = query,
            start = start,
        ).toModel()

        bookRecentSearchDataSource.addRecentSearch(query)
        result
    }

    override suspend fun removeBookRecentSearch(query: String) {
        bookRecentSearchDataSource.removeRecentSearch(query)
    }

    override suspend fun getBookDetail(itemId: String) = runSuspendCatching {
        service.getBookDetail(itemId).toModel()
    }

    override suspend fun upsertBook(bookIsbn: String, bookStatus: String) = runSuspendCatching {
        service.upsertBook(BookUpsertRequest(bookIsbn, bookStatus)).toModel()
    }

    override suspend fun getLibrary(status: String?, page: Int, size: Int) = runSuspendCatching {
        service.getLibrary(status, null, page, size).toModel()
    }

    override suspend fun searchLibrary(title: String, page: Int, size: Int) = runSuspendCatching {
        val result = service.getLibrary(null, title, page, size).toModel()

        libraryRecentSearchDataSource.addRecentSearch(title)
        result
    }

    override suspend fun removeLibraryRecentSearch(query: String) {
        libraryRecentSearchDataSource.removeRecentSearch(query)
    }
}
