package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Article
import com.example.data.model.ArticleNote
import com.example.data.model.Category
import com.example.data.model.GlossaryTerm

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val slug: String,
    val number: Int,
    val title: String,
    val category: String,
    val source: String,
    val sourceUrl: String,
    val summary: String,
    val readingTime: Int,
    val date: String,
    val framework: List<String>?,
    val notes: List<ArticleNote>
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val slug: String,
    val name: String,
    val color: String,
    val count: Int,
    val desc: String
)

@Entity(tableName = "glossary")
data class GlossaryEntity(
    @PrimaryKey val term: String,
    val desc: String,
    val source: String
)

@Entity(tableName = "sync_meta")
data class SyncMetaEntity(
    @PrimaryKey val id: String = "primary_meta",
    val updatedAt: String?,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)

// Mapping Extension Functions
fun Article.toEntity(): ArticleEntity = ArticleEntity(
    slug = slug,
    number = number,
    title = title,
    category = category,
    source = source,
    sourceUrl = sourceUrl,
    summary = summary,
    readingTime = readingTime,
    date = date,
    framework = framework,
    notes = notes
)

fun ArticleEntity.toDomain(): Article = Article(
    slug = slug,
    number = number,
    title = title,
    category = category,
    source = source,
    sourceUrl = sourceUrl,
    summary = summary,
    readingTime = readingTime,
    date = date,
    framework = framework,
    notes = notes
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    slug = slug,
    name = name,
    color = color,
    count = count,
    desc = desc
)

fun CategoryEntity.toDomain(): Category = Category(
    slug = slug,
    name = name,
    color = color,
    count = count,
    desc = desc
)

fun GlossaryTerm.toEntity(): GlossaryEntity = GlossaryEntity(
    term = term,
    desc = desc,
    source = source
)

fun GlossaryEntity.toDomain(): GlossaryTerm = GlossaryTerm(
    term = term,
    desc = desc,
    source = source
)
