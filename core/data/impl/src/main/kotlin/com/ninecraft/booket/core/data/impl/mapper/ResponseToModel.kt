package com.ninecraft.booket.core.data.impl.mapper

import com.ninecraft.booket.core.common.extensions.decodeHtmlEntities
import com.ninecraft.booket.core.common.extensions.toFormattedDate
import com.ninecraft.booket.core.model.BookDetailModel
import com.ninecraft.booket.core.model.BookSearchModel
import com.ninecraft.booket.core.model.BookSummaryModel
import com.ninecraft.booket.core.model.BookUpsertModel
import com.ninecraft.booket.core.model.Emotion
import com.ninecraft.booket.core.model.EmotionModel
import com.ninecraft.booket.core.model.HomeModel
import com.ninecraft.booket.core.model.LibraryBookSummaryModel
import com.ninecraft.booket.core.model.LibraryBooksModel
import com.ninecraft.booket.core.model.LibraryModel
import com.ninecraft.booket.core.model.PageInfoModel
import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.core.model.ReadingRecordsModel
import com.ninecraft.booket.core.model.RecentBookModel
import com.ninecraft.booket.core.model.RecordDetailModel
import com.ninecraft.booket.core.model.RecordRegisterModel
import com.ninecraft.booket.core.model.SeedModel
import com.ninecraft.booket.core.model.TermsAgreementModel
import com.ninecraft.booket.core.model.UserProfileModel
import com.ninecraft.booket.core.network.response.BookDetailResponse
import com.ninecraft.booket.core.network.response.BookSearchResponse
import com.ninecraft.booket.core.network.response.BookSummary
import com.ninecraft.booket.core.network.response.BookUpsertResponse
import com.ninecraft.booket.core.network.response.Category
import com.ninecraft.booket.core.network.response.HomeResponse
import com.ninecraft.booket.core.network.response.LibraryBookSummary
import com.ninecraft.booket.core.network.response.LibraryBooks
import com.ninecraft.booket.core.network.response.LibraryResponse
import com.ninecraft.booket.core.network.response.PageInfo
import com.ninecraft.booket.core.network.response.ReadingRecord
import com.ninecraft.booket.core.network.response.ReadingRecordsResponse
import com.ninecraft.booket.core.network.response.RecentBook
import com.ninecraft.booket.core.network.response.RecordDetailResponse
import com.ninecraft.booket.core.network.response.RecordRegisterResponse
import com.ninecraft.booket.core.network.response.SeedResponse
import com.ninecraft.booket.core.network.response.TermsAgreementResponse
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
        pubDate = pubDate,
        totalResults = totalResults,
        startIndex = startIndex,
        itemsPerPage = itemsPerPage,
        query = query,
        searchCategoryId = searchCategoryId,
        searchCategoryName = searchCategoryName,
        lastPage = lastPage,
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
        link = link,
        userBookStatus = userBookStatus,
        key = "$title-$isbn13",
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
        isbn13 = isbn13,
        mallType = mallType,
        coverImageUrl = coverImageUrl,
        categoryName = categoryName,
        publisher = publisher,
        totalPage = totalPage,
        userBookStatus = userBookStatus,
    )
}

internal fun BookUpsertResponse.toModel(): BookUpsertModel {
    return BookUpsertModel(
        userBookId = userBookId,
        userId = userId,
        isbn13 = isbn13,
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        status = status,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        createdAt = createdAt,
        updatedAt = updatedAt,
        recordCount = recordCount,
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
        isbn13 = isbn13,
        bookTitle = bookTitle,
        bookAuthor = bookAuthor,
        status = status,
        coverImageUrl = coverImageUrl,
        publisher = publisher,
        createdAt = createdAt,
        updatedAt = updatedAt,
        recordCount = recordCount,
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

internal fun ReadingRecordsResponse.toModel(): ReadingRecordsModel {
    return ReadingRecordsModel(
        content = content.map { it.toModel() },
        page = page.toModel(),
    )
}

internal fun ReadingRecord.toModel(): ReadingRecordModel {
    return ReadingRecordModel(
        id = id,
        userBookId = userBookId,
        pageNumber = pageNumber,
        quote = quote,
        review = review,
        emotionTags = emotionTags,
        createdAt = createdAt,
        updatedAt = updatedAt,
        bookTitle = bookTitle,
        bookPublisher = bookPublisher,
        bookCoverImageUrl = bookCoverImageUrl,
    )
}

internal fun RecordDetailResponse.toModel(): RecordDetailModel {
    return RecordDetailModel(
        id = id,
        userBookId = userBookId,
        pageNumber = pageNumber,
        quote = quote,
        review = review,
        emotionTags = emotionTags,
        createdAt = createdAt.toFormattedDate(),
        updatedAt = updatedAt.toFormattedDate(),
        bookTitle = bookTitle,
        bookPublisher = bookPublisher,
        bookCoverImageUrl = bookCoverImageUrl,
        author = author,
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
        isbn13 = isbn13,
        title = title,
        author = author,
        publisher = publisher,
        coverImageUrl = coverImageUrl,
        lastRecordedAt = lastRecordedAt,
        recordCount = recordCount,
    )
}

internal fun SeedResponse.toModel(): SeedModel {
    return SeedModel(
        categories = categories.mapNotNull { it.toEmotionModel() },
    )
}

internal fun Category.toEmotionModel(): EmotionModel? {
    val emotion = Emotion.fromDisplayName(name) ?: return null
    return EmotionModel(
        name = emotion,
        count = count,
    )
}

internal fun TermsAgreementResponse.toModel(): TermsAgreementModel {
    return TermsAgreementModel(
        id = id,
        email = email,
        nickname = nickname,
        provider = provider,
        termsAgreed = termsAgreed,
    )
}
