package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "slug") val slug: String,
    @Json(name = "name") val name: String,
    @Json(name = "color") val color: String,
    @Json(name = "count") val count: Int,
    @Json(name = "desc") val desc: String
)

@JsonClass(generateAdapter = true)
data class ArticleNote(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "number") val number: Int,
    @Json(name = "slug") val slug: String,
    @Json(name = "title") val title: String,
    @Json(name = "category") val category: String,
    @Json(name = "source") val source: String,
    @Json(name = "sourceUrl") val sourceUrl: String,
    @Json(name = "summary") val summary: String,
    @Json(name = "readingTime") val readingTime: Int,
    @Json(name = "date") val date: String,
    @Json(name = "framework") val framework: List<String>? = null,
    @Json(name = "notes") val notes: List<ArticleNote> = emptyList()
)

@JsonClass(generateAdapter = true)
data class GlossaryTerm(
    @Json(name = "term") val term: String,
    @Json(name = "desc") val desc: String,
    @Json(name = "source") val source: String
)

@JsonClass(generateAdapter = true)
data class NotesData(
    @Json(name = "updatedAt") val updatedAt: String? = null,
    @Json(name = "categories") val categories: List<Category> = emptyList(),
    @Json(name = "articles") val articles: List<Article> = emptyList(),
    @Json(name = "glossary") val glossary: List<GlossaryTerm> = emptyList()
)

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val message: String, val timestamp: Long) : SyncState()
    data class Error(val message: String) : SyncState()
}
