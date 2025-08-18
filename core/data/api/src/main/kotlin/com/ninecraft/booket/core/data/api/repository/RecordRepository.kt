package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.ReadingRecordModel
import com.ninecraft.booket.core.model.RecordRegisterModel
import com.ninecraft.booket.core.model.ReadingRecordsModel
import com.ninecraft.booket.core.model.RecordDetailModel

interface RecordRepository {
    suspend fun postRecord(
        userBookId: String,
        pageNumber: Int,
        quote: String,
        emotionTags: List<String>,
        review: String,
    ): Result<RecordRegisterModel>

    suspend fun getReadingRecords(
        userBookId: String,
        sort: String,
        page: Int,
        size: Int,
    ): Result<ReadingRecordsModel>

    suspend fun getRecordDetail(
        readingRecordId: String,
    ): Result<RecordDetailModel>

    suspend fun editRecord(
        readingRecordId: String,
        pageNumber: Int,
        quote: String,
        emotionTags: List<String>,
        review: String,
    ): Result<ReadingRecordModel>

    suspend fun deleteRecord(
        readingRecordId: String,
    ): Result<Unit>
}
