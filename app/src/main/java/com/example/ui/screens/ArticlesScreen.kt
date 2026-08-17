package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.theme.parseHexColor
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun ArticlesScreen(
    viewModel: NotesViewModel,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val articles by viewModel.filteredArticles.collectAsState()
    val searchQuery by viewModel.articleSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Screen Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(MemphisPink)
                        .border(BorderStroke(2.dp, MemphisBorder)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "文章总览",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.width(8.dp))
                MemphisSticker(
                    text = "${articles.size} 篇",
                    backgroundColor = MemphisYellow,
                    textColor = MemphisInk,
                    fontSize = 11
                )
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
                        onValueChange = { viewModel.setArticleSearchQuery(it) },
                        placeholder = {
                            Text(
                                "搜索文章标题、摘要或正文关键词...",
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
                            .testTag("article_search_input")
                    )
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MemphisInk,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { viewModel.setArticleSearchQuery("") }
                        )
                    }
                }
            }
        }

        item {
            // Category Filter Chips Scrollable Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "全部" Chip
                val isAllSelected = selectedCategory == null
                CategoryFilterChip(
                    label = "全部",
                    isSelected = isAllSelected,
                    accentColor = MemphisYellow,
                    onClick = { viewModel.setCategoryFilter(null) }
                )

                // Category Chips
                notesData.categories.forEach { category ->
                    val isSelected = selectedCategory == category.slug
                    CategoryFilterChip(
                        label = category.name,
                        isSelected = isSelected,
                        accentColor = parseHexColor(category.color),
                        onClick = {
                            viewModel.setCategoryFilter(if (isSelected) null else category.slug)
                        }
                    )
                }
            }
        }

        if (articles.isEmpty()) {
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
                            text = "没有找到匹配的文章",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "试试更换关键词或重置分类筛选",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MemphisInk.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        } else {
            items(articles) { article ->
                ArticleMiniCard(
                    article = article,
                    onClick = { onArticleClick(article) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CategoryFilterChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
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
                        .background(accentColor)
                        .border(BorderStroke(2.dp, MemphisBorder))
                } else {
                    Modifier
                        .background(MemphisCream)
                        .border(BorderStroke(2.dp, MemphisBorder))
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isSelected && label != "全部") {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(accentColor)
                        .border(1.dp, MemphisBorder)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = MemphisInk,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}
