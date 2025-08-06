package com.ninecraft.booket.feature.record.ocr

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.Neutral950
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.component.CameraFrame
import com.ninecraft.booket.feature.record.ocr.component.SentenceBox
import com.ninecraft.booket.feature.screens.OcrScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Ocr(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(contentWindowInsets = WindowInsets(0.dp)) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (state.currentUi) {
                OcrUi.CAMERA -> CameraPreview(state = state, modifier = modifier)
                OcrUi.RESULT -> TextScanResult(state = state, modifier = modifier)
            }
        }
    }
}

@Composable
private fun CameraPreview(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permission = android.Manifest.permission.CAMERA
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            state.eventSink(OcrUiEvent.OnCameraPermissionResult(isGranted = isGranted))
        } else {
            state.eventSink(OcrUiEvent.OnRequestPermissionDialog)
        }
    }
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { _ ->
        val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            state.eventSink(OcrUiEvent.OnCameraPermissionResult(isGranted = granted))
        } else {
            state.eventSink(OcrUiEvent.OnRequestPermissionDialog)
        }
    }

    val cameraController = remember { LifecycleCameraController(context) }
    val imageAnalyzer = remember {
        ImageAnalysis.Analyzer { imageProxy ->
            state.eventSink(OcrUiEvent.OnFrameReceived(imageProxy))
        }
    }

    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Neutral950,
            darkIcons = false,
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = White,
                darkIcons = true,
            )
        }
    }

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Neutral950,
            isNavigationBarContrastEnforced = false,
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = White,
                darkIcons = !isDarkTheme,
                isNavigationBarContrastEnforced = false,
            )
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            state.eventSink(OcrUiEvent.OnCameraPermissionResult(isGranted = true))
        } else {
            launcher.launch(permission)
        }
    }

    DisposableEffect(lifecycleOwner, cameraController) {
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            imageAnalyzer,
        )
        onDispose {
            cameraController.unbind()
            cameraController.clearImageAnalysisAnalyzer()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Neutral950)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ReedCloseTopAppBar(
                modifier = Modifier.background(color = Color.Black),
                isDark = true,
                onClose = {
                    state.eventSink(OcrUiEvent.OnCloseClick)
                },
            )
            Text(
                text = stringResource(R.string.ocr_guide),
                color = ReedTheme.colors.contentInverse,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.headline2Medium,
            )
        }

        if (state.hasPermission) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .aspectRatio(1f)
                    .align(Alignment.Center),
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            clipToOutline = true
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            controller = cameraController
                        }
                    },
                )
            }
            CameraFrame(modifier = Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isTextDetectionFailed) {
                Text(
                    text = stringResource(R.string.ocr_error_text_detection_failed),
                    color = ReedTheme.colors.contentError,
                    textAlign = TextAlign.Center,
                    style = ReedTheme.typography.label2Regular,
                )
                Spacer(modifier = Modifier.height(22.dp))
            }

            Button(
                onClick = {
                    state.eventSink(OcrUiEvent.OnCaptureButtonClick)
                },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ReedTheme.colors.bgPrimary,
                    contentColor = White,
                ),
                contentPadding = PaddingValues(ReedTheme.spacing.spacing0),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_maximize),
                    contentDescription = "Scan Icon",
                    modifier = Modifier.size(ReedTheme.spacing.spacing8),
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        }
    }

    if (state.isPermissionDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.permission_dialog_title),
            description = stringResource(R.string.permission_dialog_description),
            confirmButtonText = stringResource(R.string.permission_dialog_move_to_settings),
            onConfirmRequest = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                settingsLauncher.launch(intent)
            },
        )
    }
}

@Composable
private fun TextScanResult(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
    ) {
        ReedCloseTopAppBar(
            title = stringResource(R.string.ocr_sentence_selection),
            onClose = {
                state.eventSink(OcrUiEvent.OnCloseClick)
            },
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = ReedTheme.spacing.spacing3),
            verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
        ) {
            items(state.sentenceList.size) { index ->
                SentenceBox(
                    onClick = {
                        state.eventSink(OcrUiEvent.OnSentenceSelected(index))
                    },
                    sentence = state.sentenceList[index],
                    isSelected = state.selectedIndices.contains(index),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ReedTheme.spacing.spacing5,
                    vertical = ReedTheme.spacing.spacing4,
                ),
        ) {
            ReedButton(
                onClick = {
                    state.eventSink(OcrUiEvent.OnReCaptureButtonClick)
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.SECONDARY,
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.ocr_recapture),
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            ReedButton(
                onClick = {
                    state.eventSink(OcrUiEvent.OnSelectionConfirmed)
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                enabled = state.selectedIndices.isNotEmpty(),
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.ocr_selection_confirm),
            )
        }
    }

    if (state.isRecaptureDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.recapture_dialog_title),
            description = stringResource(R.string.recapture_dialog_description),
            confirmButtonText = stringResource(R.string.recapture_dialog_confirm),
            onConfirmRequest = {
                state.eventSink(OcrUiEvent.OnRecaptureDialogConfirmed)
            },
            dismissButtonText = stringResource(R.string.recapture_dialog_cancel),
            onDismissRequest = {
                state.eventSink(OcrUiEvent.OnRecaptureDialogDismissed)
            },
        )
    }
}

@ComponentPreview
@Composable
private fun CameraPreviewPreview() {
    ReedTheme {
        CameraPreview(
            state = OcrUiState(
                eventSink = {},
            ),
        )
    }
}

@ComponentPreview
@Composable
private fun TextRecognitionResultPreview() {
    ReedTheme {
        TextScanResult(
            state = OcrUiState(
                eventSink = {},
            ),
        )
    }
}
