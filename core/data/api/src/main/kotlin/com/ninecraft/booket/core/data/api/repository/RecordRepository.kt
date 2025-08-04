package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.RecordDetailModel
import com.ninecraft.booket.core.model.RecordRegisterModel

interface RecordRepository {
    suspend fun postRecord(
        userBookId: String,
        pageNumber: Int,
        quote: String,
        emotionTags: List<String>,
        review: String,
    ): Result<RecordRegisterModel>

    suspend fun getRecordDetail(
        readingRecordId: String,
    ): Result<RecordDetailModel>
}
