package com.ninecraft.booket.core.common.constants

import com.ninecraft.booket.core.common.R

enum class BookStatus(val value: String) {
    BEFORE_READING("BEFORE_READING"),
    READING("READING"),
    COMPLETED("COMPLETED"),
    ;

    fun getDisplayNameRes(): Int {
        return when (this) {
            BEFORE_READING -> R.string.book_status_before
            READING -> R.string.book_status_reading
            COMPLETED -> R.string.book_status_completed
        }
    }

    companion object Companion {
        fun fromValue(value: String): BookStatus? {
            return entries.find { it.value == value }
        }
    }
}
