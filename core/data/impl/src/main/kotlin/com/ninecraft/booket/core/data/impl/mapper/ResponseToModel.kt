package com.ninecraft.booket.core.data.impl.mapper

import com.ninecraft.booket.core.model.LoginModel
import com.ninecraft.booket.core.network.response.LoginResponse

internal fun LoginResponse.toModel(): LoginModel {
    return LoginModel(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}
