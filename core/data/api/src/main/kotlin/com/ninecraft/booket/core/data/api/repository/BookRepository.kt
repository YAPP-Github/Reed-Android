package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel

interface BookRepository {
    suspend fun searchBook(
        query: String,
        start: Int,
    ): Result<BookSearchModel>

    suspend fun getBookDetail(itemId: String): Result<BookDetailModel>
}
