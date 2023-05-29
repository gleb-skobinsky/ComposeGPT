package com.example.compose.composegpt.components

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.compose.composegpt.theme.ComposeGPTTheme
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

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
    Row(
        Modifier.padding(bottom = 24.dp)
            .align(Alignment.BottomCenter)
            .size(64.dp)
            .border(width = 1.dp, brush = SolidColor(lightBlue), shape = RoundedCornerShape(50))
            .drawWaves()
            /*
            .background(
                Brush.radialGradient(
                    listOf(
                        lightBlue,
                        Color.Transparent,
                    )
                ),
                RoundedCornerShape(50)
            )
             */
            .clickable {
                onAction()
            }
            .clip(RoundedCornerShape(50))

    ) {}
}

fun Modifier.drawWaves() = composed {
    val speedRatio = 1f
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f * speedRatio
            }
        }
    }
    val sinSize = 6
    val waves = 3
    drawBehind {
        val mean = size.width / 2
        val pointsDistance = size.width / sinSize
        val subStep = pointsDistance / 4
        val step = size.width / sinSize / 3
        val points = constructXPoints(
            sinSize = sinSize,
            pointsDistance = pointsDistance,
            time = time,
            mean = mean
        )

        /*
        val strokePath = Path().apply {
            for (index in points.indices) {
                val offset = points[index]
                when (index) {
                    0 -> moveTo(offset.x, offset.y)
                    points.indices.last -> lineTo(offset.x, offset.y)
                    else -> {
                        lineTo(offset.x - subStep, offset.y)
                        lineTo(offset.x + subStep, mean * 2 - offset.y)
                        // drawRect(Color.Red, Offset(offset.x, mean), Size(2.dp.toPx(), 2.dp.toPx()))
                    }
                }
            }
        }
         */
        val strokePath = Path().apply {
            for (index in points.indices) {
                val offset = points[index]
                when (index) {
                    0 -> {
                        moveTo(offset.x, offset.y)
                    }

                    points.indices.last -> {
                        val prevOffset = points[index - 1]
                        val sourceXNeg = prevOffset.x + subStep
                        val sourceYNeg = mean * 2 - prevOffset.y
                        val xMiddle = (sourceXNeg + offset.x) / 2f
                        cubicTo(xMiddle, sourceYNeg, xMiddle, offset.y, offset.x, offset.y)
                        // lineTo(offset.x, offset.y)
                    }

                    else -> {
                        val prevOffset = points[index - 1]
                        val sourceXNeg = prevOffset.x + subStep
                        val sourceYNeg = mean * 2 - prevOffset.y
                        val targetXPos = offset.x - subStep
                        val targetYPos = offset.y
                        val xMiddle1 = (sourceXNeg + targetXPos) / 2f
                        // drawDebugRect(xMiddle1, sourceYNeg)
                        cubicTo(xMiddle1, sourceYNeg, xMiddle1, targetYPos, targetXPos, targetYPos)
                        // lineTo(targetXPos, targetYPos)
                        val targetXNeg = offset.x + subStep
                        val targetYNeg = mean * 2 - offset.y
                        val xMiddle2 = (targetXPos + targetXNeg) / 2f
                        cubicTo(xMiddle2, targetYPos, xMiddle2, targetYNeg, targetXNeg, targetYNeg)
                        // lineTo(targetXNeg, targetYNeg)
                        // drawDebugRect(xMiddle2, targetYPos)
                    }
                }
            }
        }
        drawPath(
            path = strokePath,
            color = Color.White,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

fun DrawScope.drawDebugRect(x: Float, y: Float) {
    drawRect(Color.Red, Offset(x, y), Size(2.dp.toPx(), 2.dp.toPx()))
}

private fun constructXPoints(
    sinSize: Int,
    pointsDistance: Float,
    time: Float,
    mean: Float,
): MutableList<Offset> {
    val points = mutableListOf<Offset>()
    for (i in 0 until sinSize) {
        val xMin = pointsDistance * i
        val addUp = time % 1 * pointsDistance
        val offsetX = xMin + addUp
        val offsetY = Distributor.calculateY(offsetX, mean)
        points.add(Offset(offsetX, offsetY))
    }
    points.add(0, Offset(0f, mean))
    points.add(Offset(mean * 2, mean))
    return points
}

/*
private fun DrawScope.sinPath(xPoints: List<Float>, mean: Flo) {
    val yPoints = mutableListOf<Float>()

    for (i in 0..sinSize) {
        val xOffset = offset + pointsDistance * i
        val basis = Distributor.normalDistributionY(xOffset, mean).toFloat()
        yPoints.add(basis + mean)
    }
    val strokePath = Path().apply {
        moveTo(0f, mean)
        for (i in yPoints.indices) {
            val currentX = offset + i * pointsDistance
            if (i == 0) {
                val conX1 = currentX / 2f
                val conX2 = currentX / 2f
                val conY2 = yPoints[i]
                cubicTo(
                    x1 = conX1,
                    y1 = mean,
                    x2 = conX2,
                    y2 = conY2,
                    x3 = currentX,
                    y3 = yPoints[i]
                )
            } else {
                val previousX = offset + (i - 1) * pointsDistance
                val conX1 = (previousX + currentX) / 2f
                val conX2 = (previousX + currentX) / 2f
                val conY1 = yPoints[i - 1]
                val conY2 = yPoints[i]
                cubicTo(
                    x1 = conX1,
                    y1 = conY1,
                    x2 = conX2,
                    y2 = conY2,
                    x3 = currentX,
                    y3 = yPoints[i]
                )
            }
        }
    }
    drawPath(
        path = strokePath,
        color = Color.Green,
        style = Stroke(
            width = 1.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}
 */


object Distributor {
    private var positive: Boolean = true

    private fun Boolean.toInt() = when (this) {
        true -> 1
        false -> -1
    }

    fun calculateY(x: Float, mean: Float): Float {
        // positive = !positive
        val stdDev = mean / 3
        val exponent = -0.5 * ((x - mean) / stdDev).pow(2)
        val denominator = stdDev * sqrt(2 * PI)
        return mean + ((mean - 10) * exp(exponent) / denominator * 100 * positive.toInt()).toFloat()
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



