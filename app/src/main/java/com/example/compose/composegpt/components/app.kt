package com.example.compose.composegpt.components

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.input.pointer.pointerInput
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
    var speed by remember { mutableStateOf(0.5f) }
    var focused by remember { mutableStateOf(false) }
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
            .pointerInput(Unit){
                detectTapGestures(
                    onLongPress = {
                        focused = !focused
                        speed = focused.toAnimationSpeed()
                        if (focused) {
                            onAction()
                        }
                    }
                )
            }
            .clip(RoundedCornerShape(50))
            .drawWaves(speed)
    ) {}
}

private fun Boolean.toAnimationSpeed() = when (this) {
    true -> 1.5f
    false -> 0.5f
}

fun Modifier.drawWaves(speed: Float) = composed {
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f * speed
            }
        }
    }
    val sinSize = 4
    drawBehind {
        val mean = size.width / 2
        val pointsDistance = size.width / sinSize
        val subStep = pointsDistance / 4
        val step = size.width / sinSize / 3
        drawWave(sinSize, pointsDistance, time, mean, subStep, -step)
        drawWave(sinSize, pointsDistance, time, mean, subStep, 0f)
        drawWave(sinSize, pointsDistance, time, mean, subStep, step)
    }
}

private fun DrawScope.drawWave(
    sinSize: Int,
    pointsDistance: Float,
    time: Float,
    mean: Float,
    subStep: Float,
    initialOffset: Float,
) {
    val xPoints = constructXPoints(
        sinSize = sinSize,
        pointsDistance = pointsDistance,
        time = time,
        mean = mean,
        initialOffset = initialOffset
    )
    val strokePath = Path().apply {
        for (index in xPoints.indices) {
            val offsetX = xPoints[index]
            when (index) {
                0 -> {
                    val offsetY = calculateY(offsetX, mean)
                    moveTo(offsetX - subStep, offsetY)
                }

                xPoints.indices.last -> {
                    val sourceXNeg = xPoints[index - 1] + subStep
                    val sourceYNeg = mean * 2 - calculateY(sourceXNeg, mean)
                    val xMiddle = (sourceXNeg + offsetX) / 2f
                    val targetOffsetX = offsetX + subStep
                    val targetOffsetY = calculateY(targetOffsetX, mean)
                    cubicTo(xMiddle, sourceYNeg, xMiddle, targetOffsetY, targetOffsetX, targetOffsetY)
                }

                else -> {
                    val sourceXNeg = xPoints[index - 1] + subStep
                    val sourceYNeg = mean * 2 - calculateY(sourceXNeg, mean)
                    val targetXPos = offsetX - subStep
                    val targetYPos = calculateY(targetXPos, mean)
                    val xMiddle1 = (sourceXNeg + targetXPos) / 2f
                    cubicTo(xMiddle1, sourceYNeg, xMiddle1, targetYPos, targetXPos, targetYPos)
                    val targetXNeg = offsetX + subStep
                    val targetYNeg = mean * 2 - calculateY(targetXNeg, mean)
                    val xMiddle2 = (targetXPos + targetXNeg) / 2f
                    cubicTo(xMiddle2, targetYPos, xMiddle2, targetYNeg, targetXNeg, targetYNeg)
                }
            }
        }
    }
    drawPath(
        path = strokePath,
        color = Color.White,
        style = Stroke(
            width = 2f,
            cap = StrokeCap.Round
        )
    )
}

fun DrawScope.drawDebugRect(x: Float, y: Float) {
    drawRect(Color.Red, Offset(x, y), Size(2.dp.toPx(), 2.dp.toPx()))
}

private fun constructXPoints(
    sinSize: Int,
    pointsDistance: Float,
    time: Float,
    mean: Float,
    initialOffset: Float,
): MutableList<Float> {
    val points = mutableListOf<Float>()
    for (i in -2..sinSize + 1) {
        val xMin = initialOffset + pointsDistance * i
        val addUp = time % 1 * pointsDistance
        val offsetX = xMin + addUp
        // val offsetY = calculateY(offsetX, mean)
        points.add(offsetX)
    }
    // points.add(0, Offset(0f, mean))
    // points.add(Offset(mean * 2, mean))
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




private fun calculateY(x: Float, mean: Float): Float {
    val stdDev = mean / 3
    val exponent = -0.5 * ((x - mean) / stdDev).pow(2)
    val denominator = stdDev * sqrt(2 * PI)
    return mean + ((mean / 2) * exp(exponent) / denominator * 100).toFloat()
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



