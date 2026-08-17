package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MemphisCreamBg
import com.example.ui.theme.MemphisGridLine
import com.example.ui.theme.MemphisPastelPink
import com.example.ui.theme.MemphisPastelYellow
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisYellow

@Composable
fun MemphisBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MemphisCreamBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // 1. Draw subtle background grid lines
            val gridSize = 32.dp.toPx()
            var x = 0f
            while (x < canvasWidth) {
                drawLine(
                    color = MemphisGridLine.copy(alpha = 0.5f),
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 1f
                )
                x += gridSize
            }

            var y = 0f
            while (y < canvasHeight) {
                drawLine(
                    color = MemphisGridLine.copy(alpha = 0.5f),
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f
                )
                y += gridSize
            }

            // 2. Decorative geometric accents (like in the website background)
            // Left pink square accent
            drawRect(
                color = MemphisPastelPink.copy(alpha = 0.6f),
                topLeft = Offset(16.dp.toPx(), 220.dp.toPx()),
                size = Size(40.dp.toPx(), 40.dp.toPx())
            )

            // Top-right pale yellow circle
            drawCircle(
                color = MemphisPastelYellow.copy(alpha = 0.8f),
                radius = 35.dp.toPx(),
                center = Offset(canvasWidth - 30.dp.toPx(), 60.dp.toPx())
            )

            // Right triangle outline
            val triPath = Path().apply {
                moveTo(canvasWidth - 10.dp.toPx(), 280.dp.toPx())
                lineTo(canvasWidth - 35.dp.toPx(), 260.dp.toPx())
                lineTo(canvasWidth - 35.dp.toPx(), 300.dp.toPx())
                close()
            }
            drawPath(
                path = triPath,
                color = MemphisGridLine,
                style = Stroke(width = 2.dp.toPx())
            )

            // Dotted circle near center
            drawCircle(
                color = MemphisYellow.copy(alpha = 0.5f),
                radius = 16.dp.toPx(),
                center = Offset(canvasWidth * 0.6f, 380.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // Plus mark bottom left
            val plusX = 24.dp.toPx()
            val plusY = canvasHeight - 120.dp.toPx()
            val plusSize = 12.dp.toPx()
            drawLine(
                color = MemphisPink.copy(alpha = 0.4f),
                start = Offset(plusX - plusSize, plusY),
                end = Offset(plusX + plusSize, plusY),
                strokeWidth = 3.dp.toPx()
            )
            drawLine(
                color = MemphisPink.copy(alpha = 0.4f),
                start = Offset(plusX, plusY - plusSize),
                end = Offset(plusX, plusY + plusSize),
                strokeWidth = 3.dp.toPx()
            )
        }

        content()
    }
}
