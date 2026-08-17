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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.data.model.Category
import com.example.ui.components.MemphisButton
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.parseHexColor
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun CategoryScreen(
    category: Category,
    viewModel: NotesViewModel,
    onBack: () -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val notesData by viewModel.notesData.collectAsState()
    val articles = notesData.articles.filter { it.category == category.slug }
    val categoryColor = parseHexColor(category.color)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            MemphisButton(
                text = "返回全部分类",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack,
                backgroundColor = MemphisCream
            )
        }

        item {
            // Category Header Card
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                showDotGrid = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .background(categoryColor)
                            .border(BorderStroke(1.5.dp, MemphisBorder))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk
                        )

                        MemphisSticker(
                            text = "${articles.size} 篇文章",
                            backgroundColor = categoryColor,
                            textColor = Color.White,
                            fontSize = 12
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = category.desc,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MemphisInk.copy(alpha = 0.8f),
                        lineHeight = 19.sp
                    )
                }
            }
        }

        items(articles) { article ->
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
