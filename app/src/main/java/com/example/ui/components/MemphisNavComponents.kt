package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SyncState
import com.example.ui.theme.MemphisBlue
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisLilac
import com.example.ui.theme.MemphisMint
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisYellow

enum class AppTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val activeColor: Color
) {
    HOME("home", "首页", Icons.Default.Home, MemphisYellow),
    ARTICLES("articles", "文章总览", Icons.Default.GridView, MemphisPink),
    TIMELINE("timeline", "阅读时间线", Icons.Default.Timeline, MemphisBlue),
    GLOSSARY("glossary", "概念词典", Icons.Default.AutoStories, MemphisMint),
    ABOUT("about", "关于", Icons.Default.Info, MemphisLilac)
}

@Composable
fun MemphisTopBar(
    syncState: SyncState,
    onSyncClick: () -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "syncSpin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "syncSpinAngle"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MemphisCream.copy(alpha = 0.96f))
            .border(BorderStroke(0.dp, Color.Transparent))
            .drawBehind {
                // Bottom black thick border
                drawLine(
                    color = MemphisBorder,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Logo + App Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTitleClick
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .drawBehind {
                            drawRect(
                                color = MemphisInk,
                                topLeft = Offset(3.dp.toPx(), 3.dp.toPx()),
                                size = size
                            )
                        }
                        .background(MemphisInk)
                        .border(BorderStroke(2.5.dp, MemphisBorder))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tp_logo),
                        contentDescription = "TP Logo",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "阅读笔记",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = MemphisInk,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "notes.tpgofighting.top",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MemphisInk.copy(alpha = 0.55f),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Right: Sync Button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isSyncing = syncState is SyncState.Syncing
                Box(
                    modifier = Modifier
                        .testTag("sync_button")
                        .drawBehind {
                            drawRect(
                                color = MemphisInk,
                                topLeft = Offset(2.dp.toPx(), 2.dp.toPx()),
                                size = size
                            )
                        }
                        .background(if (isSyncing) MemphisPink else MemphisYellow)
                        .border(BorderStroke(2.dp, MemphisBorder))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSyncClick
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync",
                            tint = if (isSyncing) Color.White else MemphisInk,
                            modifier = Modifier
                                .size(14.dp)
                                .then(if (isSyncing) Modifier.rotate(spinAngle) else Modifier)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSyncing) "同步中" else "同步",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSyncing) Color.White else MemphisInk,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MemphisBottomNav(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MemphisCream.copy(alpha = 0.98f))
            .drawBehind {
                drawLine(
                    color = MemphisBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 3.dp.toPx()
                )
            }
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.values().forEach { tab ->
                val isSelected = currentTab == tab
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .testTag("nav_tab_${tab.route}")
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .then(
                            if (isSelected) {
                                Modifier
                                    .drawBehind {
                                        drawRect(
                                            color = MemphisInk,
                                            topLeft = Offset(2.dp.toPx(), 2.dp.toPx()),
                                            size = size
                                        )
                                    }
                                    .background(tab.activeColor)
                                    .border(BorderStroke(2.dp, MemphisBorder))
                            } else {
                                Modifier
                                    .background(Color.Transparent)
                                    .border(BorderStroke(1.5.dp, MemphisBorder.copy(alpha = 0.2f)))
                            }
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = MemphisInk,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = MemphisInk,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
