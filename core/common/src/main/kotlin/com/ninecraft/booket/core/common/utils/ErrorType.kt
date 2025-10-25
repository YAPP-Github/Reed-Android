package com.ninecraft.booket.core.common.utils

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ErrorType {
    data object NetworkError : ErrorType
    data object ServerError : ErrorType
}

