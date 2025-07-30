package com.ninecraft.booket.feature.termsagreement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.noRippleClickable
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.checkbox.SquareCheckBox
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.login.R
import com.ninecraft.booket.feature.screens.TermsAgreementScreen
import com.ninecraft.booket.feature.termsagreement.component.TermItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.collections.immutable.persistentListOf

@CircuitInject(TermsAgreementScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun TermsAgreementUi(
    state: TermsAgreementUiState,
    modifier: Modifier = Modifier,
) {
    HandleTermsAgreementSideEffects(state = state)

    val termsTitles = stringArrayResource(id = R.array.terms_agreement_items)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(76.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = ReedTheme.spacing.spacing5),
        ) {
            Text(
                text = stringResource(R.string.terms_agreement_title),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.title2SemiBold,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = ReedTheme.colors.contentBrand,
                        shape = RoundedCornerShape(ReedTheme.radius.sm),
                    )
                    .noRippleClickable {
                        state.eventSink(TermsAgreementUiEvent.OnAllTermsAgreedClick)
                    }
                    .padding(
                        horizontal = ReedTheme.spacing.spacing4,
                        vertical = ReedTheme.spacing.spacing5,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SquareCheckBox(
                    checked = state.isAllAgreed,
                    onCheckedChange = {
                        state.eventSink(TermsAgreementUiEvent.OnAllTermsAgreedClick)
                    },
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
                Text(
                    text = stringResource(R.string.terms_agreement_all),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.headline1SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            TermItem(
                title = termsTitles[0],
                checked = state.agreedTerms[0],
                onCheckClick = {
                    state.eventSink(TermsAgreementUiEvent.OnTermItemClick(0))
                },
                onDetailClick = {
                    state.eventSink(TermsAgreementUiEvent.OnPolicyClick)
                },
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            TermItem(
                title = termsTitles[1],
                checked = state.agreedTerms[1],
                onCheckClick = {
                    state.eventSink(TermsAgreementUiEvent.OnTermItemClick(1))
                },
                onDetailClick = {
                    state.eventSink(TermsAgreementUiEvent.OnTermClick)
                },
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            TermItem(
                title = termsTitles[2],
                checked = state.agreedTerms[2],
                hasDetailAction = false,
                onCheckClick = {
                    state.eventSink(TermsAgreementUiEvent.OnTermItemClick(2))
                },
            )
        }
        ReedButton(
            onClick = {
                state.eventSink(TermsAgreementUiEvent.OnStartButtonClick)
            },
            sizeStyle = largeButtonStyle,
            colorStyle = ReedButtonColorStyle.PRIMARY,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                    bottom = ReedTheme.spacing.spacing4,
                ),
            enabled = state.isAllAgreed,
            text = stringResource(R.string.terms_agreement_button_start),
        )
    }
}

@DevicePreview
@Composable
private fun TermsAgreementPreview() {
    ReedTheme {
        TermsAgreementUi(
            state = TermsAgreementUiState(
                isAllAgreed = false,
                agreedTerms = persistentListOf(false, false, false),
                eventSink = {},
            ),
        )
    }
}
