package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.data.model.Category
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisPlatformBadge
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBlue
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisMint
import com.example.ui.theme.MemphisPastelBlue
import com.example.ui.theme.MemphisPastelLilac
import com.example.ui.theme.MemphisPastelMint
import com.example.ui.theme.MemphisPastelOrange
import com.example.ui.theme.MemphisPastelPink
import com.example.ui.theme.MemphisPastelYellow
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.theme.parseHexColor
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun HomeScreen(
    viewModel: NotesViewModel,
    onCategoryClick: (Category) -> Unit,
    onArticleClick: (Article) -> Unit,
    onViewAllArticles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val articlesCount = notesData.articles.size
    val categoriesCount = notesData.categories.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // 1. Hero Banner (exact Memphis design from website)
            MemphisHeroBanner(
                articlesCount = if (articlesCount > 0) articlesCount else 43,
                categoriesCount = if (categoriesCount > 0) categoriesCount else 7
            )
        }

        item {
            // 2. "分类导航" Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MemphisMint)
                        .border(BorderStroke(2.dp, MemphisBorder)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = null,
                        tint = MemphisInk,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "分类导航",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = MemphisInk,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // 3. Category Cards Grid (7 Categories matching live site)
        items(notesData.categories) { category ->
            CategoryCardItem(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }

        item {
            // 4. "最新收录" Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(MemphisPink)
                            .border(BorderStroke(2.dp, MemphisBorder)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "最新收录",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MemphisInk
                    )
                }

                Row(
                    modifier = Modifier
                        .clickable { onViewAllArticles() }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "查看全部",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MemphisInk
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "All",
                        tint = MemphisInk,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Preview top 4 latest articles
        items(notesData.articles.take(4)) { article ->
            ArticleMiniCard(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MemphisHeroBanner(
    articlesCount: Int,
    categoriesCount: Int,
    modifier: Modifier = Modifier
) {
    MemphisCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MemphisYellow,
        borderColor = MemphisBorder,
        borderWidth = 3.dp,
        shadowOffset = 5.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Background Canvas Accents
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Top right yellow triangle
                val triangle = Path().apply {
                    moveTo(w - 20.dp.toPx(), 10.dp.toPx())
                    lineTo(w - 45.dp.toPx(), 45.dp.toPx())
                    lineTo(w - 5.dp.toPx(), 45.dp.toPx())
                    close()
                }
                drawPath(triangle, MemphisInk, style = Stroke(width = 2.dp.toPx()))

                // Dots on right side
                val dotRadius = 1.5.dp.toPx()
                val spacing = 8.dp.toPx()
                for (r in 0..3) {
                    for (c in 0..3) {
                        drawCircle(
                            color = MemphisPink.copy(alpha = 0.5f),
                            radius = dotRadius,
                            center = Offset(w - 20.dp.toPx() - (c * spacing), 70.dp.toPx() + (r * spacing))
                        )
                    }
                }

                // Wavy zigzag underline in pink
                val waveY = 115.dp.toPx()
                val wavePath = Path().apply {
                    moveTo(0f, waveY)
                    var wx = 0f
                    var up = true
                    while (wx < 140.dp.toPx()) {
                        val nextX = wx + 10.dp.toPx()
                        val nextY = if (up) waveY - 5.dp.toPx() else waveY + 5.dp.toPx()
                        lineTo(nextX, nextY)
                        wx = nextX
                        up = !up
                    }
                }
                drawPath(wavePath, MemphisPink, style = Stroke(width = 2.5.dp.toPx()))
            }

            Column {
                // Sticker Pill
                MemphisSticker(
                    text = "☷ 阅读笔记",
                    backgroundColor = MemphisPink,
                    textColor = Color.White,
                    rotation = -2f,
                    fontSize = 11
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Big Headline matching website
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$articlesCount",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk,
                        fontFamily = FontFamily.SansSerif,
                        lineHeight = 40.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "篇文章的",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk,
                        fontFamily = FontFamily.SansSerif,
                        lineHeight = 36.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(MemphisInk)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AI",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisYellow,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "工程阅读笔记",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subtitle
                Text(
                    text = "$categoriesCount 个分类，$articlesCount 篇文章，每篇含要点摘录、概念标注与原文链接。",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MemphisInk.copy(alpha = 0.85f),
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
fun CategoryCardItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = parseHexColor(category.color, MemphisBlue)
    val pastelBg = when (category.slug) {
        "ai-engineering" -> MemphisPastelBlue
        "ai-tools" -> MemphisPastelPink
        "vibe-coding" -> MemphisPastelYellow
        "design" -> MemphisPastelMint
        "industry" -> MemphisPastelOrange
        "ai-video" -> MemphisPastelLilac
        else -> MemphisPastelPink
    }

    MemphisCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_card_${category.slug}"),
        backgroundColor = pastelBg,
        borderColor = MemphisBorder,
        borderWidth = 2.5.dp,
        shadowOffset = 4.dp,
        showDotGrid = true,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Accent Color Bar
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(6.dp)
                    .background(categoryColor)
                    .border(BorderStroke(1.5.dp, MemphisBorder))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk
                )

                // Count Badge
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${category.count}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "篇",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MemphisInk.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = category.desc,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MemphisInk.copy(alpha = 0.75f),
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // "进入分类 →" action
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MemphisWhite.copy(alpha = 0.8f))
                    .border(BorderStroke(1.5.dp, MemphisBorder))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "进入分类",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = MemphisInk
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

@Composable
fun ArticleMiniCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MemphisCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("article_mini_${article.slug}"),
        backgroundColor = MemphisWhite,
        borderColor = MemphisBorder,
        borderWidth = 2.dp,
        shadowOffset = 3.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${String.format("%02d", article.number)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MemphisInk.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = article.date,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MemphisInk.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace
                    )
                }

                MemphisPlatformBadge(platform = article.source)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = MemphisInk,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.summary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = MemphisInk.copy(alpha = 0.7f),
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MemphisInk.copy(alpha = 0.5f),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${article.readingTime} 分钟",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MemphisInk.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
