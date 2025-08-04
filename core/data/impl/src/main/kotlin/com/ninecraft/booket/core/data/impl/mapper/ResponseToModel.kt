package com.ninecraft.booket.core.data.impl.mapper

import com.ninecraft.booket.core.common.extensions.decodeHtmlEntities
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.model.BookUpsertModel
import com.ninecraft.booket.core.model.HomeModel
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.model.LibraryBooksModel
import com.ninecraft.booket.core.model.LibraryModel
import com.ninecraft.booket.core.model.PageInfoModel
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.core.model.RecordRegisterModel
import com.ninecraft.booket.core.model.UserProfileModel
import com.ninecraft.booket.core.network.response.BookDetailResponse
import com.ninecraft.booket.core.network.response.BookSearchResponse
import com.ninecraft.booket.core.network.response.BookSummary
import com.ninecraft.booket.core.network.response.BookUpsertResponse
import com.ninecraft.booket.core.network.response.HomeResponse
import com.ninecraft.booket.core.network.response.LibraryBookSummary
import com.ninecraft.booket.core.network.response.LibraryBooks
import com.ninecraft.booket.core.network.response.LibraryResponse
import com.ninecraft.booket.core.network.response.PageInfo
import com.ninecraft.booket.core.network.response.RecentBook
import com.ninecraft.booket.core.network.response.RecordRegisterResponse
import com.ninecraft.booket.core.network.response.UserProfileResponse

internal fun UserProfileResponse.toModel(): UserProfileModel {
    return UserProfileModel(
        id = id,
        email = email,
        nickname = nickname,
        provider = provider,
        termsAgreed = termsAgreed,
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
        isbn13 = isbn13,
        title = title.decodeHtmlEntities(),
        author = author,
        publisher = publisher,
        coverImageUrl = coverImageUrl,
        userBookStatus = userBookStatus,
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
        books = books.toModel(),
        totalCount = totalCount,
        beforeReadingCount = beforeReadingCount,
        readingCount = readingCount,
        completedCount = completedCount,
    )
}

internal fun LibraryBooks.toModel(): LibraryBooksModel {
    return LibraryBooksModel(
        content = content.map { it.toModel() },
        page = page.toModel(),
    )
}

internal fun LibraryBookSummary.toModel(): LibraryBookSummaryModel {
    return LibraryBookSummaryModel(
        userBookId = userBookId,
        userId = userId,
        bookIsbn = bookIsbn,
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        status = status,
        recordCount = recordCount,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun PageInfo.toModel(): PageInfoModel {
    return PageInfoModel(
        size = size,
        number = number,
        totalElements = totalElements,
        totalPages = totalPages,
    )
}

internal fun RecordRegisterResponse.toModel(): RecordRegisterModel {
    return RecordRegisterModel(
        id = id,
        userBookId = userBookId,
        pageNumber = pageNumber,
        quote = quote,
        emotionTags = emotionTags,
        review = review,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

internal fun HomeResponse.toModel(): HomeModel {
    return HomeModel(
        recentBooks = recentBooks.map { it.toModel() },
    )
}

internal fun RecentBook.toModel(): RecentBookModel {
    return RecentBookModel(
        userBookId = userBookId,
        title = title,
        author = author,
        publisher = publisher,
        coverImageUrl = coverImageUrl,
        lastRecordedAt = lastRecordedAt,
        recordCount = recordCount,
    )
}
