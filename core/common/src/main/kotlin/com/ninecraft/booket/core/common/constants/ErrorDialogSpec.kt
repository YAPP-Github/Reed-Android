package com.ninecraft.booket.core.common.constants

data class ErrorDialogSpec(
    val message: String,
    val buttonLabelResId: Int,
    val action: () -> Unit,
)
