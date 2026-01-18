package com.ninecraft.booket.core.model.state

import androidx.compose.runtime.Stable

@Stable
sealed interface UserState {
    data object Guest : UserState
    data object LoggedIn : UserState
}
