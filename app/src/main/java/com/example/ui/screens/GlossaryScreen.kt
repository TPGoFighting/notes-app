package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GlossaryTerm
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisMint
import com.example.ui.theme.MemphisPastelBlue
import com.example.ui.theme.MemphisPastelLilac
import com.example.ui.theme.MemphisPastelMint
import com.example.ui.theme.MemphisPastelPink
import com.example.ui.theme.MemphisPastelYellow
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun GlossaryScreen(
    viewModel: NotesViewModel,
    onTermSourceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val glossaryList by viewModel.filteredGlossary.collectAsState()
    val searchQuery by viewModel.glossarySearchQuery.collectAsState()

    val cardBgColors = listOf(
        MemphisPastelYellow,
        MemphisPastelMint,
        MemphisPastelPink,
        MemphisPastelBlue,
        MemphisPastelLilac
    )

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
                        .background(MemphisMint)
                        .border(BorderStroke(2.dp, MemphisBorder)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = MemphisInk,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "概念词典",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "收录全部文章中出现的关键概念与术语，按字母排序。点击卡片底部的出处可跳转到对应文章的阅读笔记。",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MemphisInk.copy(alpha = 0.75f),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MemphisSticker(
                    text = "共 ${notesData.glossary.size} 个术语",
                    backgroundColor = MemphisMint,
                    textColor = MemphisInk,
                    fontSize = 11
                )

                if (searchQuery.isNotEmpty()) {
                    MemphisSticker(
                        text = "匹配 ${glossaryList.size} 个",
                        backgroundColor = MemphisPink,
                        textColor = Color.White,
                        fontSize = 11
                    )
                }
            }
        }

        item {
            // Search Input Box
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.dp,
                shadowOffset = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MemphisInk.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setGlossarySearchQuery(it) },
                        placeholder = {
                            Text(
                                "搜索术语名称或释义...",
                                fontSize = 13.sp,
                                color = MemphisInk.copy(alpha = 0.4f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("glossary_search_input")
                    )
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MemphisInk,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { viewModel.setGlossarySearchQuery("") }
                        )
                    }
                }
            }
        }

        if (glossaryList.isEmpty()) {
            item {
                MemphisCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    backgroundColor = MemphisYellow,
                    borderColor = MemphisBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "没有匹配的术语",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "换个关键词再试试。",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MemphisInk.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        } else {
            itemsIndexed(glossaryList) { index, item ->
                val bg = cardBgColors[index % cardBgColors.size]
                val sourceArticle = notesData.articles.find { it.slug == item.source }

                MemphisCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("glossary_card_${item.term}"),
                    backgroundColor = bg,
                    borderColor = MemphisBorder,
                    borderWidth = 2.dp,
                    shadowOffset = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = item.term,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.desc,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MemphisInk.copy(alpha = 0.85f),
                            lineHeight = 19.sp
                        )

                        if (sourceArticle != null) {
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MemphisWhite.copy(alpha = 0.9f))
                                    .border(BorderStroke(1.5.dp, MemphisBorder))
                                    .clickable { onTermSourceClick(item.source) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "出处: ${sourceArticle.title}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MemphisInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = MemphisInk,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
