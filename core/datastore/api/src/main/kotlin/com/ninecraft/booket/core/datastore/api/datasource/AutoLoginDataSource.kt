package com.ninecraft.booket.core.datastore.api.datasource

import com.ninecraft.booket.core.model.AutoLoginState
import kotlinx.coroutines.flow.Flow

interface AutoLoginDataSource {
    val autoLoginState: Flow<AutoLoginState>
}