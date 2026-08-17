package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY date DESC, number DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE category = :categorySlug ORDER BY date DESC, number DESC")
    fun getArticlesByCategory(categorySlug: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE slug = :slug LIMIT 1")
    fun getArticleBySlug(slug: String): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles WHERE slug = :slug LIMIT 1")
    suspend fun findArticleBySlug(slug: String): ArticleEntity?

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticlesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun clearArticles()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY count DESC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE slug = :slug LIMIT 1")
    fun getCategoryBySlug(slug: String): Flow<CategoryEntity?>

    @Query("SELECT * FROM categories WHERE slug = :slug LIMIT 1")
    suspend fun findCategoryBySlug(slug: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun clearCategories()
}

@Dao
interface GlossaryDao {
    @Query("SELECT * FROM glossary ORDER BY term ASC")
    fun getAllGlossary(): Flow<List<GlossaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlossary(terms: List<GlossaryEntity>)

    @Query("DELETE FROM glossary")
    suspend fun clearGlossary()
}

@Dao
interface SyncMetaDao {
    @Query("SELECT * FROM sync_meta WHERE id = :id LIMIT 1")
    fun getSyncMeta(id: String = "primary_meta"): Flow<SyncMetaEntity?>

    @Query("SELECT * FROM sync_meta WHERE id = :id LIMIT 1")
    suspend fun findSyncMeta(id: String = "primary_meta"): SyncMetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMeta(meta: SyncMetaEntity)
}
