package com.ninecraft.booket.feature.record.ocr

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.Neutral950
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.component.CameraFrame
import com.ninecraft.booket.feature.record.ocr.component.SentenceBox
import com.ninecraft.booket.feature.screens.OcrScreen
import com.skydoves.compose.effects.RememberedEffect
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import java.io.File
import com.ninecraft.booket.core.designsystem.R as designR

@CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun OcrUi(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    HandleOcrSideEffects(state = state)

    when (state.currentUi) {
        OcrUi.CAMERA -> CameraPreview(state = state, modifier = modifier)
        OcrUi.RESULT -> TextScanResult(state = state, modifier = modifier)
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

    /**
     * Camera Permission Request
     */
    var isGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        isGranted = granted

        if (!granted) {
            state.eventSink(OcrUiEvent.OnShowPermissionDialog)
        }
    }
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { _ -> }

    // 최초 진입 시 권한 요청
    RememberedEffect(Unit) {
        if (!isGranted) {
            state.eventSink(OcrUiEvent.OnHidePermissionDialog)
            permissionLauncher.launch(permission)
        }
    }

    // 앱이 포그라운드로 북귀할 때 OS 권한 동기화
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                if (isGranted) {
                    state.eventSink(OcrUiEvent.OnHidePermissionDialog)
                } else {
                    state.eventSink(OcrUiEvent.OnShowPermissionDialog)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    /**
     * Camera Controller
     */
    val cameraController = remember { LifecycleCameraController(context) }

    DisposableEffect(isGranted, lifecycleOwner, cameraController) {
        if (isGranted) {
            cameraController.bindToLifecycle(lifecycleOwner)
        }

        onDispose {
            cameraController.unbind()
        }
    }

    /**
     * SystemStatusBar Color
     */
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false,
            isNavigationBarContrastEnforced = false,
        )

        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true,
                isNavigationBarContrastEnforced = false,
            )
        }
    }

    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Neutral950,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ReedCloseTopAppBar(
                modifier = Modifier
                    .background(color = Color.Black)
                    .align(Alignment.TopCenter),
                isDark = true,
                onClose = {
                    state.eventSink(OcrUiEvent.OnCloseClick)
                },
            )
            Text(
                text = stringResource(R.string.ocr_guide),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-164).dp),
                color = ReedTheme.colors.contentInverse,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.headline2Medium,
            )

            if (isGranted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .height(200.dp)
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
                    enabled = !state.isLoading,
                    onClick = {
                        state.eventSink(OcrUiEvent.OnCaptureStart)

                        val executor = ContextCompat.getMainExecutor(context)
                        val photoFile = File.createTempFile("ocr_", ".jpg", context.cacheDir)
                        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                        cameraController.takePicture(
                            output,
                            executor,
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    state.eventSink(OcrUiEvent.OnImageCaptured(photoFile.toUri()))
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    state.eventSink(OcrUiEvent.OnCaptureFailed(exception))
                                }
                            },
                        )
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

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = ReedTheme.colors.contentBrand)
                }
            }
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
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    .padding(horizontal = ReedTheme.spacing.spacing5),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                item {
                    Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing1))
                }

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
