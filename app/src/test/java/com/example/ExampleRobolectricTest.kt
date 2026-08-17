package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.NotesApiClient
import com.example.data.db.AppDatabase
import com.example.data.db.toDomain
import com.example.data.db.toEntity
import com.example.data.parser.JsBundleParser
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var inMemoryDb: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        inMemoryDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        inMemoryDb.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AI工程阅读笔记", appName)
    }

    @Test
    fun `test live server scraping and parsing all 43 articles`() = runBlocking {
        val client = NotesApiClient()
        val fetched = client.fetchLatestNotesData()
        assertNotNull(fetched)
        assertTrue(fetched!!.articles.isNotEmpty())
        assertEquals(43, fetched.articles.size)
        assertEquals(7, fetched.categories.size)
        assertEquals(70, fetched.glossary.size)
    }

    @Test
    fun `verify asset notes_data can be parsed and stored in Room`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val json = context.assets.open("notes_data.json").bufferedReader().use { it.readText() }
        val parsed = JsBundleParser.parseJson(json)
        assertNotNull(parsed)
        assertTrue(parsed!!.articles.isNotEmpty())
        assertTrue(parsed.categories.isNotEmpty())

        // Insert into Room
        val articleDao = inMemoryDb.articleDao()
        val categoryDao = inMemoryDb.categoryDao()
        val glossaryDao = inMemoryDb.glossaryDao()

        articleDao.insertArticles(parsed.articles.map { it.toEntity() })
        categoryDao.insertCategories(parsed.categories.map { it.toEntity() })
        glossaryDao.insertGlossary(parsed.glossary.map { it.toEntity() })

        // Query back from Room
        val roomArticles = articleDao.getAllArticles().first()
        val roomCategories = categoryDao.getAllCategories().first()
        val roomGlossary = glossaryDao.getAllGlossary().first()

        assertEquals(parsed.articles.size, roomArticles.size)
        assertEquals(parsed.categories.size, roomCategories.size)
        assertEquals(parsed.glossary.size, roomGlossary.size)

        // Validate domain conversion
        val domainArticle = roomArticles.first().toDomain()
        assertNotNull(domainArticle.title)
    }
}
