package com.ninecraft.booket.core.data.api.repository

interface RemoteConfigRepository {
    suspend fun getLatestVersion(): Result<String>
}
