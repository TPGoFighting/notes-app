package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Article
import com.example.data.model.GlossaryTerm
import com.example.ui.components.MemphisButton
import com.example.ui.components.MemphisCard
import com.example.ui.components.MemphisPlatformBadge
import com.example.ui.components.MemphisSticker
import com.example.ui.theme.MemphisBlue
import com.example.ui.theme.MemphisBorder
import com.example.ui.theme.MemphisCream
import com.example.ui.theme.MemphisCreamBg
import com.example.ui.theme.MemphisInk
import com.example.ui.theme.MemphisLilac
import com.example.ui.theme.MemphisMint
import com.example.ui.theme.MemphisPastelBlue
import com.example.ui.theme.MemphisPastelMint
import com.example.ui.theme.MemphisPastelPink
import com.example.ui.theme.MemphisPastelYellow
import com.example.ui.theme.MemphisPink
import com.example.ui.theme.MemphisWhite
import com.example.ui.theme.MemphisYellow
import com.example.ui.theme.parseHexColor
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun ArticleDetailScreen(
    article: Article,
    viewModel: NotesViewModel,
    onBack: () -> Unit,
    onGlossaryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val notesData by viewModel.notesData.collectAsState()
    val category = notesData.categories.find { it.slug == article.category }
    val categoryColor = category?.let { parseHexColor(it.color) } ?: MemphisBlue

    // Find related glossary terms for this article
    val relatedGlossary = notesData.glossary.filter {
        it.source == article.slug || article.notes.any { note -> note.content.contains(it.term) }
    }

    var selectedTermDialog by remember { mutableStateOf<GlossaryTerm?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Back Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MemphisButton(
                    text = "返回",
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    onClick = onBack,
                    backgroundColor = MemphisCream,
                    testTag = "article_back_button"
                )

                if (article.sourceUrl.isNotEmpty()) {
                    MemphisButton(
                        text = "查看原文",
                        icon = Icons.Default.OpenInNew,
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.sourceUrl))
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        backgroundColor = MemphisMint,
                        testTag = "open_source_url_button"
                    )
                }
            }
        }

        item {
            // Article Header Card
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                topAccentColor = categoryColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            category?.let {
                                MemphisSticker(
                                    text = it.name,
                                    backgroundColor = categoryColor,
                                    textColor = Color.White,
                                    fontSize = 11
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = "#${String.format("%02d", article.number)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MemphisInk.copy(alpha = 0.6f),
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        MemphisPlatformBadge(platform = article.source)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = article.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MemphisInk.copy(alpha = 0.6f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${article.readingTime} 分钟阅读",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MemphisInk.copy(alpha = 0.7f),
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "·",
                            fontWeight = FontWeight.Bold,
                            color = MemphisInk.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = article.date,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MemphisInk.copy(alpha = 0.7f),
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Summary Block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MemphisCream)
                            .border(BorderStroke(1.5.dp, MemphisBorder))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = article.summary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MemphisInk,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // Framework / Key Takeaways if available
        if (!article.framework.isNullOrEmpty()) {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MemphisSticker(
                                text = "核心脉络 / 框架",
                                backgroundColor = MemphisYellow,
                                textColor = MemphisInk,
                                fontSize = 11
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        article.framework.forEachIndexed { index, point ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(MemphisInk)
                                        .border(BorderStroke(1.dp, MemphisBorder)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MemphisYellow,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = point,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MemphisInk,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Related Glossary Terms
        if (relatedGlossary.isNotEmpty()) {
            item {
                MemphisCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MemphisPastelMint,
                    borderColor = MemphisBorder,
                    borderWidth = 2.dp,
                    shadowOffset = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = MemphisInk,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "涉及的关键概念",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = MemphisInk
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            relatedGlossary.take(4).forEach { term ->
                                Box(
                                    modifier = Modifier
                                        .background(MemphisWhite)
                                        .border(BorderStroke(1.5.dp, MemphisBorder))
                                        .clickable { selectedTermDialog = term }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📖 ${term.term}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MemphisInk
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Notes
        itemsIndexed(article.notes) { index, note ->
            MemphisCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MemphisWhite,
                borderColor = MemphisBorder,
                borderWidth = 2.dp,
                shadowOffset = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Section Title Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MemphisCream)
                            .border(BorderStroke(1.5.dp, MemphisBorder))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = note.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = MemphisInk,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Section Content
                    Text(
                        text = note.content,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MemphisInk,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Glossary Term Dialog Popup
    selectedTermDialog?.let { term ->
        Dialog(onDismissRequest = { selectedTermDialog = null }) {
            MemphisCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                backgroundColor = MemphisPastelMint,
                borderColor = MemphisBorder,
                borderWidth = 3.dp,
                shadowOffset = 5.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MemphisSticker(
                            text = "概念释义",
                            backgroundColor = MemphisMint,
                            textColor = MemphisInk
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = term.term,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MemphisInk
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = term.desc,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MemphisInk,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MemphisButton(
                        text = "关闭",
                        onClick = { selectedTermDialog = null },
                        modifier = Modifier.align(Alignment.End),
                        backgroundColor = MemphisWhite
                    )
                }
            }
        }
    }
}
