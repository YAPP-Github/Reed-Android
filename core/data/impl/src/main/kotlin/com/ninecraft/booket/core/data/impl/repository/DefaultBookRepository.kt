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

    override suspend fun getBookDetail(isbn13: String) = runSuspendCatching {
        service.getBookDetail(isbn13).toModel()
    }

    override suspend fun upsertBook(isbn13: String, bookStatus: String) = runSuspendCatching {
        service.upsertBook(BookUpsertRequest(isbn13, bookStatus)).toModel()
    }

    override suspend fun filterLibraryBooks(status: String?, page: Int, size: Int) = runSuspendCatching {
        service.getLibraryBooks(status, null, page, size).toModel()
    }

    override suspend fun searchLibraryBooks(title: String, page: Int, size: Int) = runSuspendCatching {
        val result = service.getLibraryBooks(null, title, page, size).toModel()

        libraryRecentSearchDataSource.addRecentSearch(title)
        result
    }

    override suspend fun removeLibraryRecentSearch(query: String) {
        libraryRecentSearchDataSource.removeRecentSearch(query)
    }

    override suspend fun getHome() = runSuspendCatching {
        service.getHome().toModel()
    }

    override suspend fun getSeedsStats(userBookId: String) = runSuspendCatching {
        service.getSeedsStats(userBookId).toModel()
    }

    override suspend fun deleteBook(userBookId: String) = runSuspendCatching {
        service.deleteBook(userBookId)
    }
}
