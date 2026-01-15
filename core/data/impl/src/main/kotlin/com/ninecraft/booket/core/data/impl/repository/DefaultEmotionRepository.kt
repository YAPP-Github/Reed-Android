package com.ninecraft.booket.core.data.impl.repository

import com.ninecraft.booket.core.common.utils.runSuspendCatching
import com.ninecraft.booket.core.data.api.repository.EmotionRepository
import com.ninecraft.booket.core.data.impl.mapper.toModel
import com.ninecraft.booket.core.di.DataScope
import com.ninecraft.booket.core.model.EmotionGroupsModel
import com.ninecraft.booket.core.network.service.ReedService
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class DefaultEmotionRepository(
    private val service: ReedService,
) : EmotionRepository {
    override suspend fun getEmotions(): Result<EmotionGroupsModel> = runSuspendCatching {
        service.getEmotions().toModel()
    }
}
