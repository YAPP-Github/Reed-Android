package com.ninecraft.booket.feature.screens.arguments

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.ninecraft.booket.core.model.EmotionCode
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class RecordEditArgs(
    val id: String,
    val pageNumber: Int?,
    val quote: String,
    val review: String,
    val primaryEmotion: PrimaryEmotionArg,
    val detailEmotions: List<DetailEmotionArg>,
    val bookTitle: String,
    val bookPublisher: String,
    val bookCoverImageUrl: String,
    val author: String,
) : Parcelable

@Immutable
@Parcelize
data class PrimaryEmotionArg(
    val code: EmotionCode,
    val displayName: String,
) : Parcelable

@Immutable
@Parcelize
data class DetailEmotionArg(
    val id: String,
    val name: String,
) : Parcelable
