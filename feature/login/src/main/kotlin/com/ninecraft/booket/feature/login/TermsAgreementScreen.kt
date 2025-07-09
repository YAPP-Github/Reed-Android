package com.ninecraft.booket.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.component.checkbox.SquareCheckBox
import com.ninecraft.booket.core.designsystem.component.checkbox.TickOnlyCheckBox
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object TermsAgreementScreen : Screen {
    data class State(
        val isAllAgreed: Boolean,
        val agreedTerms: List<Boolean>,
        val isStartButtonEnabled: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object OnAllTermsAgreedClick : Event
        data class OnTermItemClick(val index: Int) : Event
        data object OnBackClick : Event
        data class OnTermDetailClick(val url: String) : Event
        data object OnStartButtonClick : Event
    }
}

@CircuitInject(TermsAgreementScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun TermsAgreement(
    state: TermsAgreementScreen.State,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        ReedBackTopAppBar(
            onNavigateBack = {
                state.eventSink(TermsAgreementScreen.Event.OnBackClick)
            },
        )
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
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
                    .padding(
                        horizontal = ReedTheme.spacing.spacing4,
                        vertical = ReedTheme.spacing.spacing5,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SquareCheckBox(
                    checked = state.isAllAgreed,
                    onCheckedChange = {
                        state.eventSink(TermsAgreementScreen.Event.OnAllTermsAgreedClick)
                    },
                )
                Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing4))
                Text(
                    text = stringResource(R.string.terms_agreement_all),
                    color = ReedTheme.colors.contentPrimary,
                    style = ReedTheme.typography.headline1SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))

            val termsTitles = stringArrayResource(id = R.array.terms_agreement_items)

            termsTitles.forEachIndexed { index, title ->
                TermItem(
                    title = title,
                    checked = state.agreedTerms[index],
                    onCheckClick = {
                        state.eventSink(TermsAgreementScreen.Event.OnTermItemClick(index))
                    },
                    onDetailClick = {
                        state.eventSink(TermsAgreementScreen.Event.OnTermDetailClick(""))
                    },
                )
            }
        }
        ReedButton(
            onClick = {
                state.eventSink(TermsAgreementScreen.Event.OnStartButtonClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                    bottom = ReedTheme.spacing.spacing4,
                ),
            colorStyle = ReedButtonColorStyle.PRIMARY,
            sizeStyle = largeButtonStyle,
            enabled = state.isStartButtonEnabled,
            text = stringResource(R.string.terms_agreement_button_start),
        )
    }
}

@Composable
private fun TermItem(
    title: String,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckClick: () -> Unit = {},
    onDetailClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableSingle {
                onDetailClick()
            }
            .padding(
                start = ReedTheme.spacing.spacing5,
                end = ReedTheme.spacing.spacing3,
                top = ReedTheme.spacing.spacing2,
                bottom = ReedTheme.spacing.spacing2,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TickOnlyCheckBox(
            checked = checked,
            onCheckedChange = { onCheckClick() },
        )
        Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = ReedTheme.colors.contentPrimary,
            style = ReedTheme.typography.body1Medium,
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = com.ninecraft.booket.core.designsystem.R.drawable.ic_chevron_right),
            contentDescription = "Navigation Icon",
            tint = Color.Unspecified,
        )
    }
}

@DevicePreview
@Composable
private fun TermsAgreementPreview() {
    ReedTheme {
        TermsAgreement(
            state = TermsAgreementScreen.State(
                isAllAgreed = false,
                agreedTerms = listOf(false, false, false),
                isStartButtonEnabled = false,
                eventSink = {},
            ),
        )
    }
}
