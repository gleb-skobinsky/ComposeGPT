package com.example.compose.composegpt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.compose.composegpt.theme.ComposeGPTTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun App(viewModel: ViewModel, onAction: () -> Unit) {
    ComposeGPTTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Text(
                "Meet Chloe, your ultimate virtual assistant",
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.TopCenter)
            )
            RecordingButton(onAction)
        }
    }
}

val lightBlue = Color(173, 216, 230)

@Composable
private fun BoxScope.RecordingButton(onAction: () -> Unit) {
    val density = LocalDensity.current
    Row(
        Modifier.padding(bottom = 24.dp)
            .align(Alignment.BottomCenter)
            .size(64.dp)
            .border(width = 1.dp, brush = SolidColor(lightBlue), shape = RoundedCornerShape(50))
            .background(
                Brush.radialGradient(
                    listOf(
                        lightBlue,
                        Color.Transparent,
                    )
                ),
                RoundedCornerShape(50)
            )
            .clickable {
                onAction()
            }
            .clip(RoundedCornerShape(50))
            .drawBehind {
                val radius = 32
                val innerRadius = 30
                drawPetal(45.0, radius, innerRadius, density, firstPetalBrush)
                drawPetal(80.0, radius, innerRadius, density, secondPetalBrush)
                drawPetal(127.0, radius, innerRadius, density, thirdPetalBrush)
            }
    ) {}
}

private fun DrawScope.drawPetal(
    angle: Double,
    radius: Int,
    innerRadius: Int,
    density: Density,
    firstPetalBrush: Brush,
) {
    val curveTargetX1 = radius + sin(Math.toRadians(angle)) * innerRadius
    val curveTargetY2 = radius + cos(Math.toRadians(angle)) * innerRadius
    val pointX2 = radius - sin(Math.toRadians(angle)) * innerRadius
    val pointY2 = radius - cos(Math.toRadians(angle)) * innerRadius
    with(density) {
        // drawRect(Color.Red, Offset(pointX2.dp.toPx(), pointY2.dp.toPx()), size = Size(2.dp.toPx(), 2.dp.toPx()))
        // drawRect(Color.Red, topLeft = Offset(32.dp.toPx(), 32.dp.toPx()), size = Size(2.dp.toPx(), 2.dp.toPx()))
        // drawRect(Color.Red, topLeft = Offset((pointX2 + 2).dp.toPx(), (pointY2 + 2).dp.toPx()), size = Size(20.dp.toPx(), 20.dp.toPx()))

        val path = Path().apply {
            moveTo(radius.dp.toPx(), radius.dp.toPx())
            quadraticBezierTo((radius + 12).dp.toPx(), (radius + 24).dp.toPx(), curveTargetX1.dp.toPx(), curveTargetY2.dp.toPx())
            quadraticBezierTo((radius + 24).dp.toPx(), (radius - 5).dp.toPx(), (radius + 3).dp.toPx(), (radius - 3).dp.toPx())
            quadraticBezierTo((radius - 28).dp.toPx(), (radius - 3).dp.toPx(), pointX2.dp.toPx(), pointY2.dp.toPx())
            quadraticBezierTo((radius - 10).dp.toPx(), (radius - 20).dp.toPx(), radius.dp.toPx(), radius.dp.toPx())
            // cubicTo((radius + 18).dp.toPx(), radius.dp.toPx(), (radius - 16).dp.toPx(), (radius - 10).dp.toPx(), pointX2.dp.toPx(), pointY2.dp.toPx())
            // relativeQuadraticBezierTo(10.dp.toPx(), 24.dp.toPx(), radius.dp.toPx(), radius.dp.toPx())
        }
        drawPath(
            path = path,
            brush = firstPetalBrush,
            style = Fill
        )
    }
}

val firstPetalBrush = Brush.linearGradient(
    listOf(
        Color.Blue.copy(alpha = 0.5f),
        Color(255, 50, 50).copy(alpha = 0.5f),
        Color.Green.copy(alpha = 0.5f)
    )
)

val secondPetalBrush = Brush.linearGradient(
    listOf(
        Color.Red.copy(alpha = 0.5f),
        Color(50, 255, 50).copy(alpha = 0.5f),
        Color.Blue.copy(alpha = 0.5f)
    )
)

val thirdPetalBrush = Brush.linearGradient(
    listOf(
        Color.Red.copy(alpha = 0.5f),
        Color(50, 50, 255).copy(alpha = 0.5f),
        Color.Green.copy(alpha = 0.5f)
    )
)

