package com.ninecraft.booket.core.data.api.repository

import com.ninecraft.booket.core.model.HomeModel

interface HomeRepository {
    suspend fun getHome(): Result<HomeModel>
}
