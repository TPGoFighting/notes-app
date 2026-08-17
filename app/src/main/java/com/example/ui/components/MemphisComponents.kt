package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow

@Composable
fun MemphisCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MemphisWhite,
    borderColor: Color = MemphisBorder,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = MemphisInk,
    shape: Shape = RoundedCornerShape(0.dp),
    topAccentColor: Color? = null,
    showDotGrid: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) 1.dp else shadowOffset,
        animationSpec = spring(),
        label = "cardShadow"
    )

    val translation by animateDpAsState(
        targetValue = if (isPressed && onClick != null) (shadowOffset - 1.dp) else 0.dp,
        animationSpec = spring(),
        label = "cardTranslation"
    )

    Box(
        modifier = modifier
            .offset(x = translation, y = translation)
            .drawBehind {
                if (currentOffset > 0.dp) {
                    // Draw solid retro drop shadow
                    drawRect(
                        color = shadowColor,
                        topLeft = Offset(currentOffset.toPx(), currentOffset.toPx()),
                        size = size
                    )
                }
            }
            .background(backgroundColor, shape)
            .border(BorderStroke(borderWidth, borderColor), shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
    ) {
        if (showDotGrid) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .drawBehind {
                        val dotRadius = 1.5.dp.toPx()
                        val spacing = 8.dp.toPx()
                        val cols = (size.width / spacing).toInt()
                        val rows = (size.height / spacing).toInt()
                        for (r in 0 until rows) {
                            for (c in 0 until cols) {
                                drawCircle(
                                    color = MemphisInk.copy(alpha = 0.15f),
                                    radius = dotRadius,
                                    center = Offset(c * spacing + spacing / 2, r * spacing + spacing / 2)
                                )
                            }
                        }
                    }
            )
        }

        if (topAccentColor != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(topAccentColor)
            )
        }

        content()
    }
}

@Composable
fun MemphisButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MemphisYellow,
    textColor: Color = MemphisInk,
    borderColor: Color = MemphisBorder,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 3.dp,
    icon: ImageVector? = null,
    testTag: String = "memphis_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentOffset by animateDpAsState(
        targetValue = if (isPressed) 1.dp else shadowOffset,
        animationSpec = spring(),
        label = "btnShadow"
    )

    val translation by animateDpAsState(
        targetValue = if (isPressed) (shadowOffset - 1.dp) else 0.dp,
        animationSpec = spring(),
        label = "btnTranslation"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .offset(x = translation, y = translation)
            .drawBehind {
                if (currentOffset > 0.dp) {
                    drawRect(
                        color = MemphisInk,
                        topLeft = Offset(currentOffset.toPx(), currentOffset.toPx()),
                        size = size
                    )
                }
            }
            .background(backgroundColor)
            .border(BorderStroke(borderWidth, borderColor))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = textColor,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun MemphisSticker(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MemphisPink,
    textColor: Color = Color.White,
    rotation: Float = 0f,
    fontSize: Int = 11
) {
    Box(
        modifier = modifier
            .rotate(rotation)
            .background(backgroundColor)
            .border(BorderStroke(2.dp, MemphisBorder))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black,
            fontSize = fontSize.sp,
            color = textColor,
            letterSpacing = 0.5.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun MemphisPlatformBadge(
    platform: String,
    modifier: Modifier = Modifier
) {
    val (label, bgColor) = when (platform.lowercase()) {
        "wechat" -> "微信公众号" to Color(0xFF07C160)
        "x" -> "X (Twitter)" to Color(0xFF000000)
        "xiaohongshu" -> "小红书" to Color(0xFFFF2442)
        "douyin" -> "抖音" to Color(0xFF161823)
        else -> platform to MemphisBorder
    }

    Row(
        modifier = modifier
            .background(MemphisCream)
            .border(BorderStroke(1.5.dp, MemphisBorder))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(bgColor)
                .border(1.dp, MemphisBorder)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MemphisInk,
            fontFamily = FontFamily.Monospace
        )
    }
}
