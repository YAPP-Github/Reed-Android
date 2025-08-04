package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookUpsertModel
import com.ninecraft.booket.core.model.HomeModel
import kotlinx.coroutines.flow.Flow
import com.ninecraft.booket.core.model.LibraryModel
import com.ninecraft.booket.core.model.SeedModel

interface BookRepository {
    val bookRecentSearches: Flow<List<String>>
    val libraryRecentSearches: Flow<List<String>>

    suspend fun searchBook(
        query: String,
        start: Int,
    ): Result<BookSearchModel>

    suspend fun removeBookRecentSearch(query: String)

    suspend fun getBookDetail(isbn: String): Result<BookDetailModel>

    suspend fun upsertBook(
        bookIsbn: String,
        bookStatus: String,
    ): Result<BookUpsertModel>

    suspend fun filterLibraryBooks(

    suspend fun searchLibraryBooks(
        title: String,
        page: Int,
        size: Int,
    ): Result<LibraryModel>

    suspend fun removeLibraryRecentSearch(query: String)

    suspend fun getHome(): Result<HomeModel>

    suspend fun getLibrary(
        status: String?,
        page: Int,
        size: Int,
    ): Result<LibraryModel>

    suspend fun getSeedsStats(userBookId: String): Result<SeedModel>
}
