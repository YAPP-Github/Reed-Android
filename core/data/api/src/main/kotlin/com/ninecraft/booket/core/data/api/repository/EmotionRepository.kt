package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.EmotionGroupsModel

interface EmotionRepository {
    suspend fun getEmotions(): Result<EmotionGroupsModel>
}
