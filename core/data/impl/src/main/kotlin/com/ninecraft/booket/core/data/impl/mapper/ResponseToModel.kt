package com.ninecraft.booket.core.data.impl.mapper

import com.ninecraft.booket.core.common.extensions.decodeHtmlEntities
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.model.BookUpsertModel
import com.ninecraft.booket.core.model.LibraryModel
import com.ninecraft.booket.core.model.UserProfileModel
import com.ninecraft.booket.core.network.response.BookDetailResponse
import com.ninecraft.booket.core.network.response.BookSearchResponse
import com.ninecraft.booket.core.network.response.BookSummary
import com.ninecraft.booket.core.network.response.BookUpsertResponse
import com.ninecraft.booket.core.network.response.LibraryResponse
import com.ninecraft.booket.core.network.response.UserProfileResponse

internal fun UserProfileResponse.toModel(): UserProfileModel {
    return UserProfileModel(
        id = id,
        email = email,
        nickname = nickname,
        provider = provider,
    )
}

internal fun BookSearchResponse.toModel(): BookSearchModel {
    return BookSearchModel(
        version = version,
        title = title,
        link = link,
        pubDate = pubDate,
        totalResults = totalResults,
        startIndex = startIndex,
        itemsPerPage = itemsPerPage,
        query = query,
        searchCategoryId = searchCategoryId,
        searchCategoryName = searchCategoryName,
        books = books.map { it.toModel() },
    )
}

internal fun BookSummary.toModel(): BookSummaryModel {
    return BookSummaryModel(
        isbn = isbn,
        title = title.decodeHtmlEntities(),
        author = author,
        publisher = publisher,
        coverImageUrl = coverImageUrl,
    )
}

internal fun BookDetailResponse.toModel(): BookDetailModel {
    return BookDetailModel(
        version = version,
        title = title,
        link = link,
        author = author,
        pubDate = pubDate,
        description = description,
        isbn = isbn,
        isbn13 = isbn13,
        itemId = itemId,
        priceSales = priceSales,
        priceStandard = priceStandard,
        mallType = mallType,
        stockStatus = stockStatus,
        mileage = mileage,
        cover = cover,
        categoryId = categoryId,
        categoryName = categoryName,
        publisher = publisher,
    )
}

internal fun BookUpsertResponse.toModel(): BookUpsertModel {
    return BookUpsertModel(
        userBookId = userBookId,
        userId = userId,
        bookIsbn = bookIsbn,
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        status = status,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun LibraryResponse.toModel(): LibraryModel {
    return LibraryModel(
        userBookId = userBookId,
        userId = userId,
        bookIsbn = bookIsbn,
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        status = status,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
