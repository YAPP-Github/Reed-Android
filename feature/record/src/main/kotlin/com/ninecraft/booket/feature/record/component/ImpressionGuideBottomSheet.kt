package com.ninecraft.booket.feature.record.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.ninecraft.booket.core.common.extensions.clickableSingle
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.component.ReedBottomSheet
import com.ninecraft.booket.feature.record.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import com.ninecraft.booket.core.designsystem.R as designR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpressionGuideBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    impressionGuideList: ImmutableList<String>,
    selectedImpressionGuide: String,
    onGuideClick: (Int) -> Unit,
    onCloseButtonClick: () -> Unit,
    onSelectionConfirmButtonClick: () -> Unit,
) {
    ReedBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = ReedTheme.spacing.spacing5,
                    top = ReedTheme.spacing.spacing5,
                    end = ReedTheme.spacing.spacing5,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.impression_guide_bottomsheet_title),
                    color = ReedTheme.colors.contentPrimary,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.heading2SemiBold,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_close),
                    contentDescription = "Close Icon",
                    modifier = Modifier.clickableSingle {
                        onCloseButtonClick()
                    },
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
            Text(
                text = stringResource(R.string.impression_guide_bottomsheet_description),
                modifier = Modifier.fillMaxWidth(),
                color = ReedTheme.colors.contentPrimary,
                style = ReedTheme.typography.label1Medium,
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                impressionGuideList.forEachIndexed { index, guide ->
                    ImpressionGuideBox(
                        onClick = {
                            onGuideClick(index)
                        },
                        impressionText = guide,
                        isSelected = selectedImpressionGuide == guide,
                    )
                }
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing3))
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
            ReedButton(
                onClick = {
                    onSelectionConfirmButtonClick()
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedImpressionGuide.isNotEmpty(),
                text = stringResource(R.string.impression_guide_bottomsheet_selection_confirm),
            )
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreview
@Composable
private fun ImpressionGuideBottomSheetPreview() {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        initialValue = SheetValue.Expanded,
        positionalThreshold = { 0f },
        velocityThreshold = { 0f },
    )
    val impressionGuideList = listOf(
        "에서 위로 받았다",
        "이 마음에 남았다",
        "에서 작가의 의도가 궁금하다",
        "에 대한 다른 사람들의 생각이 궁금하다",
        "에서 크게 공감이 된다",
        "을 보고 예전 기억이 났다",
        "에서 문장에 머물렀다",
    ).toPersistentList()

    ReedTheme {
        ImpressionGuideBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            impressionGuideList = impressionGuideList,
            selectedImpressionGuide = "",
            onGuideClick = {},
            onCloseButtonClick = {},
            onSelectionConfirmButtonClick = {},
        )
    }
}
