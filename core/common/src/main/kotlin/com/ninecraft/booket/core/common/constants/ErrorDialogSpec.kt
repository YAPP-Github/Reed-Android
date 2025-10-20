package com.ninecraft.booket.core.common.constants

import androidx.annotation.StringRes

data class ErrorDialogSpec(
    val title: String? = null,
    val message: String,
    @StringRes val buttonLabelResId: Int,
    val action: () -> Unit,
)
