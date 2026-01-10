package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.ReadingRecordModelV2
import com.ninecraft.booket.core.model.ReadingRecordsModel

interface RecordRepository {
    suspend fun postRecord(
        userBookId: String,
        pageNumber: Int?,
        quote: String,
        review: String,
        primaryEmotion: String,
        detailEmotionTagIds: List<String>,
    ): Result<ReadingRecordModelV2>

    suspend fun getReadingRecords(
        userBookId: String,
        sort: String,
        page: Int,
        size: Int,
    ): Result<ReadingRecordsModel> // TODO: V2로 변경 필요

    suspend fun getRecordDetail(
        readingRecordId: String,
    ): Result<ReadingRecordModelV2>

    suspend fun editRecord(
        readingRecordId: String,
        pageNumber: Int?,
        quote: String,
        review: String,
        primaryEmotion: String,
        detailEmotionTagIds: List<String>,
    ): Result<ReadingRecordModelV2>

    suspend fun deleteRecord(
        readingRecordId: String,
    ): Result<Unit>
}
