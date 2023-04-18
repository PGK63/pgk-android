package ru.pgk63.core_ui.view.metaBalls.comonents

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.metaBalls.MetaContainer
import ru.pgk63.core_ui.view.metaBalls.MetaEntity

@Composable
fun LoadingUi() {

    MetaContainer(
        color = PgkTheme.colors.tintColor
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            val x = 9

            MetaEntity(
                modifier = Modifier.fillMaxSize(),
                blur = 40f,
                metaContent = {
                    val animation = rememberInfiniteTransition()
                    val rotation by animation.animateFloat(
                        initialValue = 0f,
                        targetValue = -360f,
                        animationSpec = infiniteRepeatable(
                            tween(8000, easing = LinearEasing),
                            RepeatMode.Restart,
                        )
                    )

                    Box(
                        modifier = Modifier.size(300.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(modifier = Modifier
                            .rotate(rotation)
                            .drawBehind {
                                drawArc(
                                    color = Color.Black,
                                    startAngle = 0f,
                                    sweepAngle = 215f,
                                    useCenter = false,

                                    style = Stroke(
                                        width = size.width * .195f,
                                        cap = StrokeCap.Round
                                    )
                                )
                            }
                            .size(250.dp)
                        )
                        for (i in 0..x) {
                            Circle(i * (360f / x))
                        }
                    }
                }) {

            }
        }
    }
}

@Composable
private fun Circle(offset: Float) {
    val animation = rememberInfiniteTransition()
    val rotation by animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(8000, easing = LinearEasing),
            RepeatMode.Restart,
        )
    )

    Box(
        modifier = Modifier
            .rotate(offset)
            .rotate(rotation)
            .width(300.dp)
    ) {
        Box(
            modifier = Modifier
                .background(PgkTheme.colors.tintColor, CircleShape)
                .size(50.dp)
        )
    }
}