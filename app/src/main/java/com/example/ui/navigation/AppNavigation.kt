package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.AppTab
import com.example.ui.components.MemphisBackground
import com.example.ui.components.MemphisBottomNav
import com.example.ui.components.MemphisTopBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.ArticlesScreen
import com.example.ui.screens.CategoryScreen
import com.example.ui.screens.GlossaryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TimelineScreen
import com.example.ui.viewmodel.NotesViewModel

@Composable
fun AppNavigation(
    viewModel: NotesViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val currentArticle by viewModel.currentArticle.collectAsState()
    val currentCategory by viewModel.currentCategory.collectAsState()

    // Handle System Back Button
    BackHandler(enabled = currentArticle != null || currentCategory != null || activeTab != AppTab.HOME) {
        if (currentArticle != null) {
            viewModel.selectArticle(null)
        } else if (currentCategory != null) {
            viewModel.selectCategory(null)
        } else if (activeTab != AppTab.HOME) {
            viewModel.setTab(AppTab.HOME)
        }
    }

    MemphisBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                MemphisTopBar(
                    syncState = syncState,
                    onSyncClick = { viewModel.syncNow() },
                    onTitleClick = {
                        viewModel.selectArticle(null)
                        viewModel.selectCategory(null)
                        viewModel.setTab(AppTab.HOME)
                    }
                )
            },
            bottomBar = {
                if (currentArticle == null && currentCategory == null) {
                    MemphisBottomNav(
                        currentTab = activeTab,
                        onTabSelected = { tab ->
                            viewModel.selectArticle(null)
                            viewModel.selectCategory(null)
                            viewModel.setTab(tab)
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (currentArticle != null) {
                    ArticleDetailScreen(
                        article = currentArticle!!,
                        viewModel = viewModel,
                        onBack = { viewModel.selectArticle(null) },
                        onGlossaryClick = { slug ->
                            viewModel.selectArticle(null)
                            viewModel.setTab(AppTab.GLOSSARY)
                        }
                    )
                } else if (currentCategory != null) {
                    CategoryScreen(
                        category = currentCategory!!,
                        viewModel = viewModel,
                        onBack = { viewModel.selectCategory(null) },
                        onArticleClick = { article ->
                            viewModel.selectArticle(article)
                        }
                    )
                } else {
                    AnimatedContent(
                        targetState = activeTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tabTransition"
                    ) { tab ->
                        when (tab) {
                            AppTab.HOME -> HomeScreen(
                                viewModel = viewModel,
                                onCategoryClick = { category ->
                                    viewModel.selectCategory(category)
                                },
                                onArticleClick = { article ->
                                    viewModel.selectArticle(article)
                                },
                                onViewAllArticles = {
                                    viewModel.setCategoryFilter(null)
                                    viewModel.setTab(AppTab.ARTICLES)
                                }
                            )

                            AppTab.ARTICLES -> ArticlesScreen(
                                viewModel = viewModel,
                                onArticleClick = { article ->
                                    viewModel.selectArticle(article)
                                }
                            )

                            AppTab.TIMELINE -> TimelineScreen(
                                viewModel = viewModel,
                                onArticleClick = { article ->
                                    viewModel.selectArticle(article)
                                }
                            )

                            AppTab.GLOSSARY -> GlossaryScreen(
                                viewModel = viewModel,
                                onTermSourceClick = { sourceSlug ->
                                    viewModel.selectArticleBySlug(sourceSlug)
                                }
                            )

                            AppTab.ABOUT -> AboutScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
