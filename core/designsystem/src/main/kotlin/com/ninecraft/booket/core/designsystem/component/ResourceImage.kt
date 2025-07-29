package com.ninecraft.booket.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ninecraft.booket.core.designsystem.ComponentPreview
import com.ninecraft.booket.core.designsystem.R
import com.ninecraft.booket.core.designsystem.theme.ReedTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin

@Composable
fun ResourceImage(
    @DrawableRes imageRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholder: Painter? = painterResource(R.drawable.ic_placeholder),
    contentScale: ContentScale = ContentScale.Crop,
) {
    CoilImage(
        imageModel = { imageRes },
        modifier = modifier,
        component = rememberImageComponent {
            +PlaceholderPlugin.Loading(placeholder)
            +PlaceholderPlugin.Failure(placeholder)
        },
        imageOptions = ImageOptions(
            contentScale = contentScale,
            alignment = Alignment.Center,
            contentDescription = contentDescription,
        ),
        previewPlaceholder = placeholder,
    )
}

@ComponentPreview
@Composable
private fun ResourceImagePreview() {
    ReedTheme {
        ResourceImage(
            imageRes = R.drawable.ic_placeholder,
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_placeholder),
        )
    }
}
