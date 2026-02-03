package com.ninecraft.booket.feature.record.ocr.content

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.theme.Neutral950
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedDialog
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.OcrUiEvent
import com.ninecraft.booket.feature.record.ocr.OcrUiState
import com.ninecraft.booket.feature.record.ocr.component.CameraBottomBar
import com.ninecraft.booket.feature.record.ocr.component.CameraFrame
import com.skydoves.compose.stability.runtime.TraceRecomposition
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import java.io.File

@TraceRecomposition
@Composable
internal fun OcrCameraContent(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permission = android.Manifest.permission.CAMERA

    /**
     * Camera Permission Request
     */
    val isGranted by produceState(
        initialValue = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED,
        key1 = lifecycleOwner, // lifecycle 변경 시 재설정
    ) {
        // 최초 동기화
        value = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        // 포그라운드 복귀 시 OS 권한 동기화
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                value = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                if (value) {
                    state.eventSink(OcrUiEvent.OnHidePermissionDialog)
                } else {
                    state.eventSink(OcrUiEvent.OnShowPermissionDialog)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        awaitDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (!granted) {
            state.eventSink(OcrUiEvent.OnShowPermissionDialog)
        }
    }
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { _ -> }

    // 최초 진입 시 권한 요청
    LaunchedEffect(Unit) {
        if (!isGranted) {
            state.eventSink(OcrUiEvent.OnHidePermissionDialog)
            permissionLauncher.launch(permission)
        }
    }

    /**
     * Camera Setup (ProcessCameraProvider + Preview + ImageCapture)
     */
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }
    val preview = remember {
        Preview.Builder().build().also {
            it.setSurfaceProvider { request ->
                surfaceRequest = request
            }
        }
    }
    val imageCapture = remember { ImageCapture.Builder().build() }

    LaunchedEffect(isGranted) {
        if (!isGranted) return@LaunchedEffect
        ProcessCameraProvider.awaitInstance(context).apply {
            unbindAll()
            bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture,
            )
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

    /**
     * Image Picker
     */
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
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (-164).dp.roundToPx(),
                        )
                    },
                color = ReedTheme.colors.contentInverse,
                textAlign = TextAlign.Center,
                style = ReedTheme.typography.headline2Medium,
            )

            if (isGranted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                        .height(200.dp)
                        .align(Alignment.Center),
                ) {
                    surfaceRequest?.let { request ->
                        CameraXViewfinder(
                            surfaceRequest = request,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
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
                CameraBottomBar(
                    onGalleryClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                    onCaptureClick = {
                        state.eventSink(OcrUiEvent.OnCaptureStart)

                        val executor = ContextCompat.getMainExecutor(context)
                        val photoFile = File.createTempFile("ocr_", ".jpg", context.cacheDir)
                        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                        imageCapture.takePicture(
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
                    buttonEnabled = !state.isLoading,
                )

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

    if (state.isCameraRecognitionFailedDialogVisible) {
        ReedDialog(
            title = stringResource(R.string.ocr_recognition_failed_dialog_title),
            description = stringResource(R.string.ocr_recognition_failed_dialog_description),
            confirmButtonText = stringResource(R.string.ocr_recognition_failed_dialog_direct_input),
            onConfirmRequest = {
                state.eventSink(OcrUiEvent.OnCloseClick)
            },
            dismissButtonText = stringResource(R.string.ocr_recognition_failed_dialog_camera),
            onDismissRequest = {
                state.eventSink(OcrUiEvent.OnCameraRecognitionFailedDialogDismissed)
            },
        )
    }
}

@ComponentPreview
@Composable
private fun OcrCameraContentPreview() {
    ReedTheme {
        OcrCameraContent(
            state = OcrUiState(
                eventSink = {},
            ),
        )
    }
}
