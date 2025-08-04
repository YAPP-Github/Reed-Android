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
    val year = this.substringBefore("-")
    return "${year}년"
}
