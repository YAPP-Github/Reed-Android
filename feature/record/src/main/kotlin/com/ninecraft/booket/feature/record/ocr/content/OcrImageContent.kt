package com.ninecraft.booket.feature.record.ocr.content

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.NetworkImage
import com.ninecraft.booket.core.designsystem.theme.Black
import com.ninecraft.booket.core.designsystem.theme.Neutral950
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.OcrUiEvent
import com.ninecraft.booket.feature.record.ocr.OcrUiState
import com.ninecraft.booket.feature.record.ocr.component.ImageProcessingLoader

@Composable
internal fun OcrImageContent(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                state.eventSink(OcrUiEvent.OnImageSelected(uri.toString()))
            }
        },
    )

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Neutral950,
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            ReedCloseTopAppBar(
                modifier = Modifier
                    .background(color = Color.Black),
                isDark = true,
                onClose = {
                    state.eventSink(OcrUiEvent.OnImageContentClosed)
                },
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                NetworkImage(
                    imageUrl = state.selectedImage,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxWidth(),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Black.copy(alpha = 0.5f))
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.ocr_image_scanning),
                        color = ReedTheme.colors.contentInverse,
                        style = ReedTheme.typography.headline2Medium,
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                    ImageProcessingLoader()
                }
            }
        }
    }

    if (state.isGalleryRecognitionFailedDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.ocr_recognition_failed_dialog_title),
            description = stringResource(R.string.ocr_recognition_failed_dialog_description),
            confirmButtonText = stringResource(R.string.ocr_recognition_failed_dialog_direct_input),
            onConfirmRequest = {
                state.eventSink(OcrUiEvent.OnCloseClick)
            },
            dismissButtonText = stringResource(R.string.ocr_recognition_failed_dialog_image),
            onDismissRequest = {
                state.eventSink(OcrUiEvent.OnImageRecognitionFailedDialogDismissed)
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
        )
    }
}

@ComponentPreview
@Composable
private fun OcrImageContentPreview() {
    ReedTheme {
        OcrImageContent(
            state = OcrUiState(
                eventSink = {},
            ),
        )
    }
}
