package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.data.model.Article
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisPlatformBadge
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBlue
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.theme.parseHexColor
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun TimelineScreen(
    viewModel: NotesViewModel,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val timelineMap by viewModel.timelineArticlesByDate.collectAsState()
    val selectedCategory by viewModel.timelineCategoryFilter.collectAsState()

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
                        .background(MemphisBlue)
                        .border(BorderStroke(2.dp, MemphisBorder)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "阅读时间线",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "按发布日期排列的 ${notesData.articles.size} 篇文章。每个几何标记对应一篇文章，颜色代表分类。点击卡片跳转到详情页。",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MemphisInk.copy(alpha = 0.75f),
                lineHeight = 17.sp
            )
        }

        item {
            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isAllSelected = selectedCategory == null
                CategoryFilterChip(
                    label = "全部",
                    isSelected = isAllSelected,
                    accentColor = MemphisYellow,
                    onClick = { viewModel.setTimelineCategoryFilter(null) }
                )

                notesData.categories.forEach { category ->
                    val isSelected = selectedCategory == category.slug
                    CategoryFilterChip(
                        label = category.name,
                        isSelected = isSelected,
                        accentColor = parseHexColor(category.color),
                        onClick = {
                            viewModel.setTimelineCategoryFilter(if (isSelected) null else category.slug)
                        }
                    )
                }
            }
        }

        // Timeline Items grouped by Date
        timelineMap.forEach { (date, articles) ->
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Date Header Badge
                    MemphisSticker(
                        text = "📅 $date (${articles.size} 篇)",
                        backgroundColor = MemphisYellow,
                        textColor = MemphisInk,
                        fontSize = 11
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    articles.forEachIndexed { _, article ->
                        val cat = notesData.categories.find { it.slug == article.category }
                        val catColor = cat?.let { parseHexColor(it.color) } ?: MemphisBlue

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Left Timeline Node
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(28.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(catColor)
                                        .border(BorderStroke(2.dp, MemphisBorder))
                                )
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(80.dp)
                                        .background(MemphisBorder)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Right Article Card
                            MemphisCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("timeline_article_${article.slug}"),
                                backgroundColor = MemphisWhite,
                                borderColor = MemphisBorder,
                                borderWidth = 2.dp,
                                shadowOffset = 3.dp,
                                onClick = { onArticleClick(article) }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        cat?.let {
                                            Text(
                                                text = it.name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = catColor
                                            )
                                        }
                                        MemphisPlatformBadge(platform = article.source)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = article.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MemphisInk,
                                        lineHeight = 19.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = MemphisInk.copy(alpha = 0.5f),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${article.readingTime} min",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MemphisInk.copy(alpha = 0.6f),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
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
