package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookUpsertModel
import kotlinx.coroutines.flow.Flow
import com.ninecraft.booket.core.model.LibraryModel

interface BookRepository {
    val recentSearches: Flow<List<String>>

    suspend fun searchBook(
        query: String,
        start: Int,
    ): Result<BookSearchModel>

    suspend fun removeRecentSearch(query: String)

    suspend fun getBookDetail(itemId: String): Result<BookDetailModel>

    suspend fun upsertBook(
        bookIsbn: String,
        bookStatus: String,
    ): Result<BookUpsertModel>

    suspend fun getLibrary(
        status: String?,
        page: Int,
        size: Int,
    ): Result<LibraryModel>

    suspend fun searchLibrary(
        title: String,
        page: Int,
        size: Int,
    ): Result<LibraryModel>
}
