package com.ninecraft.booket.core.common.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.decodeHtmlEntities(): String {
    return this
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
        .replace("&#x27;", "'")
        .replace("&#x2F;", "/")
        .replace("&#39;", "'")
        .replace("&nbsp;", " ")
}

fun String.toFormattedDate(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val parsedDate = LocalDateTime.parse(this, inputFormatter)
        parsedDate.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        ""
    }
}
