package com.ninecraft.booket.core.common.constants

data class ErrorDialogSpec(
    val message: String,
    val buttonLabel: String,
    val action: () -> Unit,
)
