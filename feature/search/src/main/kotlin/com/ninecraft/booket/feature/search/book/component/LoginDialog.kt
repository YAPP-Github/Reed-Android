package com.ninecraft.booket.feature.search.book.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ninecraft.booket.core.designsystem.component.button.ReedButton
import com.ninecraft.booket.core.designsystem.component.button.ReedButtonColorStyle
import com.ninecraft.booket.core.designsystem.component.button.largeButtonStyle
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.core.ui.ReedScaffold
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import com.ninecraft.booket.core.designsystem.R as designR

@Composable
internal fun LoginDialog(
    onDismissRequest: () -> Unit,
    onKakaoLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true,
            isNavigationBarContrastEnforced = true,
        )

        onDispose {}
    }

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = DialogProperties(
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        ReedScaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = White,
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(modifier = modifier.fillMaxSize()) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(designR.drawable.img_reed_logo_big),
                                contentDescription = "Reed Logo",
                                modifier = Modifier.height(67.14.dp),
                            )
                            Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing5))
                            Text(
                                text = stringResource(designR.string.login_reed_slogan),
                                color = ReedTheme.colors.contentBrand,
                                style = ReedTheme.typography.headline2SemiBold,
                            )
                        }
                        ReedButton(
                            onClick = {
                                onKakaoLoginButtonClick()
                            },
                            sizeStyle = largeButtonStyle,
                            colorStyle = ReedButtonColorStyle.KAKAO,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = ReedTheme.spacing.spacing5,
                                    end = ReedTheme.spacing.spacing5,
                                ),
                            text = stringResource(id = designR.string.kakao_login),
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = designR.drawable.ic_kakao),
                                    contentDescription = "Kakao Icon",
                                    tint = Color.Unspecified,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
