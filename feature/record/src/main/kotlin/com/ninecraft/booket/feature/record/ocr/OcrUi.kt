package com.ninecraft.booket.feature.record.ocr

import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R as designR
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.Neutral950
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.component.ReedCloseTopAppBar
import com.ninecraft.booket.core.ui.component.ReedFullScreen
import com.ninecraft.booket.feature.record.R
import com.ninecraft.booket.feature.record.ocr.component.SentenceBox
import com.ninecraft.booket.feature.screens.OcrScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(OcrScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun Ocr(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    ReedFullScreen {
        when (state.currentUi) {
            OcrUi.CAMERA -> CameraPreview(state = state, modifier = modifier)
            OcrUi.RESULT -> TextScanResult(state = state, modifier = modifier)
        }
    }
}

@Composable
private fun CameraPreview(
    state: OcrUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val permission = android.Manifest.permission.CAMERA

    var hasPermission by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            launcher.launch(permission)
        } else {
            hasPermission = true
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral950),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .aspectRatio(1f)
                .align(Alignment.Center),
        ) {
            if (hasPermission)
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            clipToOutline = true
                            // setBackgroundColor(Color.BLACK)
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }.also { previewView ->
                            cameraController.bindToLifecycle(lifecycleOwner)
                            previewView.controller = cameraController
                        }
                    },
                )
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Button(
                onClick = {
                    state.eventSink(OcrUiEvent.OnCapture)
                },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ReedTheme.colors.bgPrimary,
                    contentColor = White,
                ),
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(designR.drawable.ic_maximize),
                    contentDescription = "Scan Icon",
                    modifier = Modifier.size(32.dp),
                )
            }
            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing4))
        }
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
            .background(White),
    ) {
        ReedCloseTopAppBar(
            title = stringResource(R.string.ocr_sentence_selection),
            onClose = {
                state.eventSink(OcrUiEvent.OnCloseClick)
            },
        )
        Column {
            LazyColumn(
                modifier = Modifier.padding(
                    vertical = ReedTheme.spacing.spacing3,
                    horizontal = ReedTheme.spacing.spacing3,
                ),
                verticalArrangement = Arrangement.spacedBy(ReedTheme.spacing.spacing2),
            ) {
                items(state.sentenceList) {
                    SentenceBox(
                        onClick = {},
                        sentence = it,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
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
                    state.eventSink(OcrUiEvent.OnReCapture)
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.SECONDARY,
                modifier = Modifier.weight(1f),
                text = "다시 촬영하기",
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing2))
            ReedButton(
                onClick = {
                    // TODO: 감정 선택 화면으로 이동
                },
                sizeStyle = largeButtonStyle,
                colorStyle = ReedButtonColorStyle.PRIMARY,
                enabled = false,
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.ocr_selection_complete),
            )
        }
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
