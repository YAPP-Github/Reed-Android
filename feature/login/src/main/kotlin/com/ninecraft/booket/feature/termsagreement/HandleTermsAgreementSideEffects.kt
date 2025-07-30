package com.ninecraft.booket.feature.termsagreement

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ninecraft.booket.feature.login.KakaoLoginClient
import com.ninecraft.booket.feature.login.LoginSideEffect
import com.ninecraft.booket.feature.login.LoginUiEvent
import com.ninecraft.booket.feature.login.LoginUiState
import com.skydoves.compose.effects.RememberedEffect

@Composable
internal fun HandleTermsAgreementSideEffects(
    state: TermsAgreementUiState,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is TermsAgreementSideEffect.ShowToast -> {
                Toast.makeText(context, state.sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            null -> {}
        }
    }
}
