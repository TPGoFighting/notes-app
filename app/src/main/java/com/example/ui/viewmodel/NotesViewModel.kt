package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Article
import com.example.data.model.Category
import com.example.data.model.GlossaryTerm
import com.example.data.model.NotesData
import com.example.data.model.SyncState
import com.example.data.repository.NotesRepository
import com.example.ui.components.AppTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val repository = NotesRepository(application.applicationContext)

    val notesData: StateFlow<NotesData> = repository.notesData
    val syncState: StateFlow<SyncState> = repository.syncState
    val serverUrl: StateFlow<String> = repository.serverUrl

    private val _activeTab = MutableStateFlow(AppTab.HOME)
    val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

    private val _articleSearchQuery = MutableStateFlow("")
    val articleSearchQuery: StateFlow<String> = _articleSearchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _timelineCategoryFilter = MutableStateFlow<String?>(null)
    val timelineCategoryFilter: StateFlow<String?> = _timelineCategoryFilter.asStateFlow()

    private val _glossarySearchQuery = MutableStateFlow("")
    val glossarySearchQuery: StateFlow<String> = _glossarySearchQuery.asStateFlow()

    private val _currentArticle = MutableStateFlow<Article?>(null)
    val currentArticle: StateFlow<Article?> = _currentArticle.asStateFlow()

    private val _currentCategory = MutableStateFlow<Category?>(null)
    val currentCategory: StateFlow<Category?> = _currentCategory.asStateFlow()

    // Filtered articles
    val filteredArticles: StateFlow<List<Article>> = combine(
        notesData,
        _articleSearchQuery,
        _selectedCategoryFilter
    ) { data, query, catSlug ->
        val q = query.trim().lowercase()
        data.articles.filter { article ->
            val matchesCategory = catSlug == null || article.category == catSlug
            val matchesQuery = q.isEmpty() ||
                    article.title.lowercase().contains(q) ||
                    article.summary.lowercase().contains(q) ||
                    article.notes.any { it.title.lowercase().contains(q) || it.content.lowercase().contains(q) }
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filtered glossary
    val filteredGlossary: StateFlow<List<GlossaryTerm>> = combine(
        notesData,
        _glossarySearchQuery
    ) { data, query ->
        val q = query.trim().lowercase()
        if (q.isEmpty()) {
            data.glossary
        } else {
            data.glossary.filter { term ->
                term.term.lowercase().contains(q) ||
                term.desc.lowercase().contains(q) ||
                term.source.lowercase().contains(q)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Timeline articles grouped by date
    val timelineArticlesByDate: StateFlow<Map<String, List<Article>>> = combine(
        notesData,
        _timelineCategoryFilter
    ) { data, catSlug ->
        val filtered = if (catSlug == null) {
            data.articles
        } else {
            data.articles.filter { it.category == catSlug }
        }
        filtered.groupBy { it.date }
            .toSortedMap(compareByDescending { it })
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    init {
        // Automatically attempt background sync when app starts
        viewModelScope.launch {
            repository.syncFromServer()
        }
    }

    fun setTab(tab: AppTab) {
        _activeTab.value = tab
    }

    fun setArticleSearchQuery(q: String) {
        _articleSearchQuery.value = q
    }

    fun setCategoryFilter(slug: String?) {
        _selectedCategoryFilter.value = slug
    }

    fun setTimelineCategoryFilter(slug: String?) {
        _timelineCategoryFilter.value = slug
    }

    fun setGlossarySearchQuery(q: String) {
        _glossarySearchQuery.value = q
    }

    fun selectArticle(article: Article?) {
        _currentArticle.value = article
    }

    fun selectArticleBySlug(slug: String) {
        _currentArticle.value = repository.getArticleBySlug(slug)
    }

    fun selectCategory(category: Category?) {
        _currentCategory.value = category
    }

    fun selectCategoryBySlug(slug: String) {
        _currentCategory.value = repository.getCategoryBySlug(slug)
    }

    fun syncNow(customUrl: String? = null) {
        viewModelScope.launch {
            repository.syncFromServer(customUrl)
        }
    }

    fun setServerUrl(url: String) {
        repository.setServerUrl(url)
    }
}
