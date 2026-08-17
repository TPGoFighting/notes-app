package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SyncState
import com.example.ui.components.MemphisButton
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisPlatformBadge
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBlue
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisLilac
import com.example.ui.theme.MemphisMint
import com.example.ui.theme.MemphisPastelLilac
import com.example.ui.theme.MemphisPastelMint
import com.example.ui.theme.MemphisPastelYellow
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun AboutScreen(
    viewModel: NotesViewModel,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val serverUrl by viewModel.serverUrl.collectAsState()

    var customUrlInput by remember(serverUrl) { mutableStateOf(serverUrl) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(MemphisLilac)
                        .border(BorderStroke(2.dp, MemphisBorder)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MemphisInk,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "关于",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // Card 1: 站点说明
        item {
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisPastelYellow,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "站点说明",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "本站收录 ${notesData.articles.size} 篇 AI 工程相关文章的阅读笔记，按主题分类整理。每篇文章提炼为结构化笔记，附概念索引与关联关系，方便检索与回顾。",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MemphisInk.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Card 2: 文章来源
        item {
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "文章来源",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MemphisPlatformBadge(platform = "wechat")
                        MemphisPlatformBadge(platform = "x")
                        MemphisPlatformBadge(platform = "xiaohongshu")
                        MemphisPlatformBadge(platform = "douyin")
                    }
                }
            }
        }

        // Card 3: 孟菲斯设计
        item {
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisPastelMint,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "孟菲斯设计",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "孟菲斯设计起源于 1980 年代，以高对比色彩、粗黑边框、几何装饰为特征。本应用完全沿用 notes.tpgofighting.top 网站风格，用明快色块与硬阴影营造信息终端的视觉秩序。",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MemphisInk.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Card 4: 服务器连接与每日同步
        item {
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisPastelLilac,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "服务器与内容同步",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk
                        )
                        MemphisSticker(
                            text = "每日更新",
                            backgroundColor = MemphisPink,
                            textColor = Color.White,
                            fontSize = 10
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "当前服务器端点: $serverUrl",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MemphisInk.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "同步状态: " + when (syncState) {
                            is SyncState.Idle -> "已就绪"
                            is SyncState.Syncing -> "正在连接服务器下载最新笔记..."
                            is SyncState.Success -> (syncState as SyncState.Success).message
                            is SyncState.Error -> (syncState as SyncState.Error).message
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (syncState is SyncState.Error) Color.Red else MemphisInk
                    )

                    if (notesData.updatedAt != null) {
                        Text(
                            text = "最后同步时间: ${notesData.updatedAt}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MemphisInk.copy(alpha = 0.6f),
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input to change server URL
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MemphisWhite)
                            .border(BorderStroke(2.dp, MemphisBorder))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        TextField(
                            value = customUrlInput,
                            onValueChange = {
                                customUrlInput = it
                                viewModel.setServerUrl(it)
                            },
                            placeholder = { Text("输入服务器 URL...") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    MemphisButton(
                        text = "立刻与服务器同步",
                        icon = Icons.Default.CloudSync,
                        onClick = { viewModel.syncNow(customUrlInput) },
                        backgroundColor = MemphisYellow,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Card 5: 技术栈
        item {
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "技术栈与构建",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Kotlin + Jetpack Compose (Material 3)",
                        "Memphis / Neobrutalism UI System",
                        "OkHttp3 + Moshi JSON Serialization",
                        "Live JS/JSON Bundle Dynamic Extractor",
                        "Offline-First Local Cache Storage",
                        "GitHub: https://github.com/TPGoFighting/notes.git"
                    ).forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(MemphisInk)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MemphisInk.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
