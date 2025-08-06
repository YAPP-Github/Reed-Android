package com.ninecraft.booket.feature.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.ui.ReedScaffold
import com.ninecraft.booket.core.ui.component.ReedBackTopAppBar
import com.ninecraft.booket.feature.screens.WebViewScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(WebViewScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun WebViewUi(
    state: WebViewUiState,
    modifier: Modifier = Modifier,
) {
    ReedScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ReedBackTopAppBar(
                title = state.title,
                onBackClick = {
                    state.eventSink(WebViewUiEvent.OnBackButtonClick)
                },
            )
        }
    ) { innerPadding ->
        WebViewContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebViewContent(
    state: WebViewUiState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    // 기본 웹뷰 클라이언트
                    webViewClient = WebViewClient()
                    settings.apply {
                        // JavaScript 실행 허용
                        javaScriptEnabled = true
                        // DOM 스토리지 허용 - 사용자 설정/세션 정보 저장
                        domStorageEnabled = true
                        // 뷰포트 메타태그 지원 - 반응형 레이아웃 조정
                        useWideViewPort = true
                        // 화면에 맞게 조정 - 모바일 최적화
                        loadWithOverviewMode = true
                    }
                    loadUrl(state.url)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@DevicePreview
@Composable
private fun WebViewUiPreview() {
    ReedTheme {
        WebViewUi(
            state = WebViewUiState(
                url = "https://m.naver.com",
                title = "개인정보처리방침",
                eventSink = {},
            ),
        )
    }
}
