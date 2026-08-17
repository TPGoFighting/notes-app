package com.example.data.api

import android.util.Log
import com.example.data.model.NotesData
import com.example.data.parser.JsBundleParser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class NotesApiClient(
    baseUrl: String = DEFAULT_BASE_URL
) {
    companion object {
        private const val TAG = "NotesApiClient"
        const val DEFAULT_BASE_URL = "https://notes.tpgofighting.top/"
    }

    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .header("Accept", "*/*")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private var currentBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
    private var retrofit: Retrofit = buildRetrofit(currentBaseUrl)
    private var apiService: NotesApiService = retrofit.create(NotesApiService::class.java)

    private fun buildRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    fun updateBaseUrl(newUrl: String) {
        val sanitized = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
        if (sanitized != currentBaseUrl) {
            currentBaseUrl = sanitized
            retrofit = buildRetrofit(sanitized)
            apiService = retrofit.create(NotesApiService::class.java)
        }
    }

    fun getService(): NotesApiService = apiService

    /**
     * Attempts to fetch NotesData from the live server.
     * 1. First tries direct JSON endpoint /notes_data.json
     * 2. If that fails or returns 404, fetches the homepage HTML, detects the Vite index-*.js bundle,
     *    downloads it and extracts the embedded data.
     */
    suspend fun fetchLatestNotesData(): NotesData? {
        // Strategy 1: Direct JSON endpoint
        try {
            val directResponse = apiService.getNotesDataDirect()
            if (directResponse.isSuccessful && directResponse.body() != null) {
                val data = directResponse.body()!!
                if (data.articles.isNotEmpty()) {
                    Log.d(TAG, "Successfully fetched direct JSON with ${data.articles.size} articles")
                    return data
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Direct JSON strategy skipped: ${e.message}")
        }

        // Strategy 2: Scrape JS bundle via Retrofit
        try {
            val htmlResponse = apiService.getIndexHtml()
            val html = htmlResponse.string()

            // Find JS bundle path in HTML (matches src="/assets/index-D7hD67q1.js" or similar)
            val pattern = Pattern.compile("src=[\"']([^\"']*(?:assets/index|assets/main)[^\"']*\\.js)[\"']")
            val matcher = pattern.matcher(html)
            var jsPath = if (matcher.find()) matcher.group(1) else null

            // Fallback general pattern if specific assets/index is not found
            if (jsPath == null) {
                val generalPattern = Pattern.compile("<script[^>]+src=[\"']([^\"']+\\.js)[\"']")
                val generalMatcher = generalPattern.matcher(html)
                if (generalMatcher.find()) {
                    jsPath = generalMatcher.group(1)
                }
            }

            if (jsPath != null) {
                val fullJsUrl = if (jsPath.startsWith("http://") || jsPath.startsWith("https://")) {
                    jsPath
                } else {
                    val trimmed = jsPath.removePrefix("/")
                    "$currentBaseUrl$trimmed"
                }

                val jsResponse = apiService.getJsBundle(fullJsUrl)
                val jsCode = jsResponse.string()

                val extracted = JsBundleParser.parseJsBundle(jsCode)
                if (extracted != null && extracted.articles.isNotEmpty()) {
                    Log.d(TAG, "Successfully parsed JS bundle with ${extracted.articles.size} articles")
                    return extracted
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed during JS bundle scraping: ${e.message}", e)
            throw e
        }

        return null
    }
}
