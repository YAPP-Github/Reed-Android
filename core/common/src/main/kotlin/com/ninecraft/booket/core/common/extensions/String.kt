package com.ninecraft.booket.core.common.extensions

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

fun String.formatPublishYear(): String {
    return try {
        val year = this.substringBefore("-")
        "${year}년"
    } catch (e: Exception) {
        this
    }
}
