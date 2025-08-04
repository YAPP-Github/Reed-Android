package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.RecordRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.network.request.RecordRegisterRequest
import com.ninecraft.booket.core.network.service.ReedService
import javax.inject.Inject

class DefaultRecordRepository @Inject constructor(
    private val service: ReedService,
) : RecordRepository {
    override suspend fun postRecord(
        userBookId: String,
        pageNumber: Int,
        quote: String,
        emotionTags: List<String>,
        review: String,
    ) = runSuspendCatching {
        service.postRecord(userBookId, RecordRegisterRequest(pageNumber, quote, emotionTags, review)).toModel()
    }

    override suspend fun getReadingRecords(
        userBookId: String,
        sort: String,
        page: Int,
        size: Int,
    ) = runSuspendCatching {
        service.getReadingRecords(userBookId, sort, page, size).toModel()
    }
}
