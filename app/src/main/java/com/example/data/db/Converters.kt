package com.example.data.db

import androidx.room.TypeConverter
import com.example.data.model.ArticleNote
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter = moshi.adapter<List<String>>(stringListType)

    private val articleNoteListType = Types.newParameterizedType(List::class.java, ArticleNote::class.java)
    private val articleNoteListAdapter = moshi.adapter<List<ArticleNote>>(articleNoteListType)

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return if (list == null) "[]" else stringListAdapter.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        return if (json.isNullOrEmpty()) emptyList() else {
            try {
                stringListAdapter.fromJson(json) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    @TypeConverter
    fun fromArticleNoteList(list: List<ArticleNote>?): String {
        return if (list == null) "[]" else articleNoteListAdapter.toJson(list)
    }

    @TypeConverter
    fun toArticleNoteList(json: String?): List<ArticleNote> {
        return if (json.isNullOrEmpty()) emptyList() else {
            try {
                articleNoteListAdapter.fromJson(json) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
