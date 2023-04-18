package ru.pgk63.core_ui.view

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.customBlur(blur: Float) = this.then(
    graphicsLayer {
        if (blur > 0f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                renderEffect = RenderEffect
                    .createBlurEffect(
                        blur,
                        blur,
                        Shader.TileMode.DECAL,
                    )
                    .asComposeRenderEffect()
            }
    }
)