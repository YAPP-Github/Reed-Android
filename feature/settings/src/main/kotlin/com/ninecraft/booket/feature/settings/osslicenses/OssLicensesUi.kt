package com.ninecraft.booket.feature.settings.osslicenses

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.ninecraft.booket.core.designsystem.DevicePreview
import com.ninecraft.booket.core.designsystem.component.appbar.ReedBackTopAppBar
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.ninecraft.booket.core.designsystem.theme.White
import com.ninecraft.booket.feature.settings.R
import com.ninecraft.booket.feature.screens.OssLicensesScreen
import com.orhanobut.logger.Logger
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.IOException

@CircuitInject(OssLicensesScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun OssLicenses(
    state: OssLicensesUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var licenses by remember { mutableStateOf<List<OssLicenseInfo>>(emptyList()) }

    LaunchedEffect(Unit) {
        licenses = withContext(Dispatchers.IO) {
            getOssLicensesDataFromAsset(context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
    ) {
        ReedBackTopAppBar(
            title = stringResource(R.string.oss_licenses_title),
            onBackClick = {
                state.eventSink(OssLicensesUiEvent.OnBackClicked)
            },
        )
        LazyColumn {
            items(licenses) { license ->
                OssLicenseItem(
                    name = license.name,
                    license = license.license,
                    url = license.url,
                )
            }
        }
    }
}

@Composable
private fun OssLicenseItem(
    name: String,
    license: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = ReedTheme.spacing.spacing3,
                vertical = ReedTheme.spacing.spacing3,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(ReedTheme.spacing.spacing1)
                    .background(
                        color = ReedTheme.colors.contentBrand,
                        shape = CircleShape,
                    ),
            )
            Spacer(modifier = Modifier.width(ReedTheme.spacing.spacing1))
            Text(
                text = "$name - $license",
                fontWeight = FontWeight.W500,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = ReedTheme.typography.caption2Regular,
            )
        }
        Spacer(modifier = Modifier.height(ReedTheme.spacing.spacing2))
        Text(
            text = url,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ReedTheme.colors.bgSecondary,
                    shape = RoundedCornerShape(ReedTheme.radius.xs),
                )
                .padding(
                    horizontal = ReedTheme.spacing.spacing2,
                    vertical = ReedTheme.spacing.spacing4,
                ),
            style = ReedTheme.typography.caption2Regular,
        )
    }
}

private fun getOssLicensesDataFromAsset(context: Context): List<OssLicenseInfo> {
    return try {
        val json = context.assets.open("oss_licenses.json")
            .bufferedReader()
            .use { it.readText() }
        Json.decodeFromString(json)
    } catch (e: IOException) {
        Logger.e(e, "Failed to read json file")
        emptyList()
    }
}

@DevicePreview
@Composable
private fun OssLicensesScreenPreview() {
    ReedTheme {
        OssLicenses(
            state = OssLicensesUiState(
                eventSink = {},
            ),
        )
    }
}
