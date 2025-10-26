package com.ninecraft.booket.core.common.extensions

import com.ninecraft.booket.core.common.utils.ErrorType
import com.ninecraft.booket.core.common.utils.isNetworkError

fun Throwable.toErrorType(): ErrorType {
    return if (this.isNetworkError()) {
        ErrorType.NetworkError
    } else {
        ErrorType.ServerError
    }
}
