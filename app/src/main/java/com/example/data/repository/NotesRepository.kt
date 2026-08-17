package com.example.data.repository

import android.content.Context
import com.example.data.api.NotesApiClient
import com.example.data.db.AppDatabase
import com.example.data.db.SyncMetaEntity
import com.example.data.db.toDomain
import com.example.data.db.toEntity
import com.example.data.model.Article
import com.example.data.model.Category
import com.example.data.model.GlossaryTerm
import com.example.data.model.NotesData
import com.example.data.model.SyncState
import com.example.data.parser.JsBundleParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class NotesRepository(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    // 1. Room Database & DAOs
    private val database: AppDatabase = AppDatabase.getInstance(context)
    val articleDao = database.articleDao()
    val categoryDao = database.categoryDao()
    val glossaryDao = database.glossaryDao()
    val syncMetaDao = database.syncMetaDao()

    // 2. Retrofit + Moshi API Client
    val apiClient: NotesApiClient = NotesApiClient()

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _serverUrl = MutableStateFlow(NotesApiClient.DEFAULT_BASE_URL.removeSuffix("/"))
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    private val _notesData = MutableStateFlow(NotesData())
    val notesData: StateFlow<NotesData> = _notesData.asStateFlow()

    init {
        // Observe Room changes reactively and reflect into _notesData StateFlow
        scope.launch {
            combine(
                articleDao.getAllArticles(),
                categoryDao.getAllCategories(),
                glossaryDao.getAllGlossary(),
                syncMetaDao.getSyncMeta()
            ) { articleEntities, categoryEntities, glossaryEntities, meta ->
                val articles = articleEntities.map { it.toDomain() }
                val categories = categoryEntities.map { it.toDomain() }
                val glossary = glossaryEntities.map { it.toDomain() }
                NotesData(
                    updatedAt = meta?.updatedAt,
                    categories = categories,
                    articles = articles,
                    glossary = glossary
                )
            }.collect { data ->
                if (data.articles.isNotEmpty()) {
                    _notesData.value = data
                }
            }
        }

        // Initialize local database if empty
        scope.launch {
            initializeDatabaseIfEmpty()
        }
    }

    fun setServerUrl(url: String) {
        val sanitized = url.trim().trimEnd('/')
        _serverUrl.value = sanitized
        apiClient.updateBaseUrl(sanitized)
    }

    /**
     * Initializes Room database from bundled asset if the database currently has 0 articles.
     */
    private suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val count = articleDao.getArticlesCount()
        if (count == 0) {
            try {
                // Try reading from bundled asset notes_data.json
                context.assets.open("notes_data.json").use { stream ->
                    val json = stream.bufferedReader().use { it.readText() }
                    val parsed = JsBundleParser.parseJson(json)
                    if (parsed != null && parsed.articles.isNotEmpty()) {
                        persistNotesDataToRoom(parsed)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Saves NotesData into Room Database (Articles, Categories, Glossary, Meta)
     */
    suspend fun persistNotesDataToRoom(data: NotesData) = withContext(Dispatchers.IO) {
        if (data.categories.isNotEmpty()) {
            categoryDao.insertCategories(data.categories.map { it.toEntity() })
        }
        if (data.articles.isNotEmpty()) {
            articleDao.insertArticles(data.articles.map { it.toEntity() })
        }
        if (data.glossary.isNotEmpty()) {
            glossaryDao.insertGlossary(data.glossary.map { it.toEntity() })
        }
        syncMetaDao.insertSyncMeta(
            SyncMetaEntity(
                id = "primary_meta",
                updatedAt = data.updatedAt ?: "刚刚更新",
                lastSyncTimestamp = System.currentTimeMillis()
            )
        )
    }

    /**
     * Syncs latest notes data from server using Retrofit and Moshi client,
     * and saves the results into the Room database.
     */
    suspend fun syncFromServer(customUrl: String? = null) {
        _syncState.value = SyncState.Syncing
        withContext(Dispatchers.IO) {
            try {
                val targetUrl = customUrl ?: _serverUrl.value
                apiClient.updateBaseUrl(targetUrl)

                val fetchedData = apiClient.fetchLatestNotesData()

                if (fetchedData != null && fetchedData.articles.isNotEmpty()) {
                    // Save into Room Database
                    persistNotesDataToRoom(fetchedData)

                    // Also save local cache file as backup
                    try {
                        val cacheFile = File(context.filesDir, "cached_notes_data.json")
                        val adapter = apiClient.moshi.adapter(NotesData::class.java)
                        cacheFile.writeText(adapter.toJson(fetchedData))
                    } catch (_: Exception) {}

                    _syncState.value = SyncState.Success(
                        message = "已成功从服务器获取并存入本地 Room 数据库 (${fetchedData.articles.size} 篇文章)",
                        timestamp = System.currentTimeMillis()
                    )
                } else {
                    _syncState.value = SyncState.Error("未能从服务器解析到有效内容，保持本地 Room 数据")
                }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error("同步失败: ${e.localizedMessage ?: "网络连接异常"}")
            }
        }
    }

    fun getArticleBySlug(slug: String): Article? {
        return _notesData.value.articles.find { it.slug == slug }
    }

    fun getCategoryBySlug(slug: String): Category? {
        return _notesData.value.categories.find { it.slug == slug }
    }

    fun getArticlesByCategory(categorySlug: String): List<Article> {
        return _notesData.value.articles.filter { it.category == categorySlug }
    }
}
