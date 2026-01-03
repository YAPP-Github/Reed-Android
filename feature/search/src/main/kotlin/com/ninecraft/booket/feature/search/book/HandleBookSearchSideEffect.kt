package com.ninecraft.booket.feature.search.book

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.ninecraft.booket.feature.search.BuildConfig
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleBookSearchSideEffects(
    state: BookSearchUiState,
    eventSink: (BookSearchUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is BookSearchSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            is BookSearchSideEffect.NavigateToKakaoTalkChannel -> {
                uriHandler.openUri(BuildConfig.REED_KAKAOTALK_CHANNEL_URL)
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(BookSearchUiEvent.InitSideEffect)
        }
    }
}
