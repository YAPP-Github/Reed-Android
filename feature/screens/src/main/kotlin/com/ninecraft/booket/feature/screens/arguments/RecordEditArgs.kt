package com.ninecraft.booket.feature.screens.arguments

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class RecordEditArgs(
    val id: String,
    val pageNumber: Int,
    val quote: String,
    val review: String,
    val emotionTags: List<String>,
    val bookTitle: String,
    val bookPublisher: String,
    val bookCoverImageUrl: String,
    val author: String,
) : Parcelable
