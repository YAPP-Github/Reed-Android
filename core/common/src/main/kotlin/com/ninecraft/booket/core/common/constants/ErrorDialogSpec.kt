package com.ninecraft.booket.core.common.constants

import androidx.annotation.StringRes

data class ErrorDialogSpec(
    val message: String,
    @StringRes val buttonLabelResId: Int,
    val action: () -> Unit,
)
