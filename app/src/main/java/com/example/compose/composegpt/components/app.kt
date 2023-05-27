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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.compose.composegpt.theme.ComposeGPTTheme

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
            ) {}
        }
    }
}

val lightBlue = Color(173, 216, 230)

