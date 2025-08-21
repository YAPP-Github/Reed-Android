package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookUpsertModel
import com.ninecraft.booket.core.model.HomeModel
import com.ninecraft.booket.core.model.LibraryModel
import com.ninecraft.booket.core.model.SeedModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    val bookRecentSearches: Flow<List<String>>
    val libraryRecentSearches: Flow<List<String>>

    suspend fun searchBook(
        query: String,
        start: Int,
    ): Result<BookSearchModel>

    suspend fun deleteBookRecentSearch(query: String)

    suspend fun getBookDetail(isbn13: String): Result<BookDetailModel>

    suspend fun upsertBook(
        isbn13: String,
        bookStatus: String,
    ): Result<BookUpsertModel>

    suspend fun filterLibraryBooks(
        status: String?,
        page: Int,
        size: Int,
    ): Result<LibraryModel>

    suspend fun searchLibraryBooks(
        title: String,
        page: Int,
        size: Int,
    ): Result<LibraryModel>

    suspend fun deleteLibraryRecentSearch(query: String)

    suspend fun getHome(): Result<HomeModel>

    suspend fun getSeedsStats(userBookId: String): Result<SeedModel>

    suspend fun deleteBook(userBookId: String): Result<Unit>
}
