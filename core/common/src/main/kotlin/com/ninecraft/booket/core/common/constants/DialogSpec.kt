package com.ninecraft.booket.core.common.constants

data class DialogSpec(
    val message: String,
    val confirmLabel: String,
    val onConfirm: () -> Unit,
    val title: String? = null,
    val dismissLabel: String? = null,
    val onDismissRequest: () -> Unit = {},
)
