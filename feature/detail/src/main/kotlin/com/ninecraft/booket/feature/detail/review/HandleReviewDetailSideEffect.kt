package com.ninecraft.booket.feature.detail.review

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleReviewDetailSideEffects(
    state: ReviewDetailUiState,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is ReviewDetailSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
