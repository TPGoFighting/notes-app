package com.example.data.parser

import com.example.data.model.Article
import com.example.data.model.Category
import com.example.data.model.GlossaryTerm
import com.example.data.model.NotesData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsBundleParser {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val notesDataAdapter = moshi.adapter(NotesData::class.java)

    private val articleListType = Types.newParameterizedType(List::class.java, Article::class.java)
    private val articleListAdapter = moshi.adapter<List<Article>>(articleListType)

    private val categoryListType = Types.newParameterizedType(List::class.java, Category::class.java)
    private val categoryListAdapter = moshi.adapter<List<Category>>(categoryListType)

    private val glossaryListType = Types.newParameterizedType(List::class.java, GlossaryTerm::class.java)
    private val glossaryListAdapter = moshi.adapter<List<GlossaryTerm>>(glossaryListType)

    fun parseJson(jsonString: String): NotesData? {
        return try {
            notesDataAdapter.fromJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun parseJsBundle(jsContent: String): NotesData? {
        try {
            // 1. Extract Articles
            val artStart = jsContent.indexOf("[{number:")
            val articles: List<Article> = if (artStart != -1) {
                val artRaw = extractBalancedArray(jsContent, artStart)
                val artJson = jsToJson(artRaw)
                articleListAdapter.fromJson(artJson) ?: emptyList()
            } else {
                emptyList()
            }

            // 2. Extract Categories
            val catStart = jsContent.indexOf("[{slug:`ai-engineering`")
                .takeIf { it != -1 } ?: jsContent.indexOf("[{slug:\"ai-engineering\"")
                .takeIf { it != -1 } ?: jsContent.indexOf("[{slug:'ai-engineering'")

            val categories: List<Category> = if (catStart != -1) {
                val catRaw = extractBalancedArray(jsContent, catStart)
                val catJson = jsToJson(catRaw)
                categoryListAdapter.fromJson(catJson) ?: emptyList()
            } else {
                emptyList()
            }

            // 3. Extract Glossary
            val termStart = jsContent.indexOf("[{term:")
            val glossary: List<GlossaryTerm> = if (termStart != -1) {
                val termRaw = extractBalancedArray(jsContent, termStart)
                val termJson = jsToJson(termRaw)
                glossaryListAdapter.fromJson(termJson) ?: emptyList()
            } else {
                emptyList()
            }

            if (articles.isNotEmpty() || categories.isNotEmpty()) {
                val nowStr = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date())
                return NotesData(
                    updatedAt = nowStr,
                    categories = categories,
                    articles = articles,
                    glossary = glossary
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun extractBalancedArray(code: String, startIndex: Int): String {
        var depth = 0
        var inString = false
        var quoteChar = ' '
        var isEscaped = false
        var end = startIndex

        for (i in startIndex until code.length) {
            val char = code[i]
            if (isEscaped) {
                isEscaped = false
                continue
            }
            if (char == '\\') {
                isEscaped = true
                continue
            }
            if (inString) {
                if (char == quoteChar) {
                    inString = false
                }
                continue
            } else {
                if (char == '`' || char == '"' || char == '\'') {
                    inString = true
                    quoteChar = char
                    continue
                }
                if (char == '[') {
                    depth++
                } else if (char == ']') {
                    depth--
                    if (depth == 0) {
                        end = i + 1
                        break
                    }
                }
            }
        }
        return if (end > startIndex) code.substring(startIndex, end) else ""
    }

    /**
     * Converts a JS object/array literal (with backticks, unquoted keys, trailing commas)
     * strictly into valid JSON format without corrupting strings.
     */
    fun jsToJson(jsCode: String): String {
        val out = StringBuilder(jsCode.length + 100)
        var i = 0
        val len = jsCode.length

        while (i < len) {
            val c = jsCode[i]

            // 1. Handle String literals: backticks `, double quotes ", single quotes '
            if (c == '`' || c == '"' || c == '\'') {
                val quoteChar = c
                val strContent = StringBuilder()
                i++
                while (i < len) {
                    val sc = jsCode[i]
                    if (sc == '\\' && i + 1 < len) {
                        val next = jsCode[i + 1]
                        if (quoteChar == '`' && next == '`') {
                            // \` inside template literal -> `
                            strContent.append('`')
                            i += 2
                            continue
                        } else {
                            strContent.append('\\').append(next)
                            i += 2
                            continue
                        }
                    }
                    if (sc == quoteChar) {
                        i++
                        break
                    }
                    strContent.append(sc)
                    i++
                }
                // Emit as valid JSON string
                out.append(escapeJsonString(strContent.toString()))
                continue
            }

            // 2. Handle JS Identifier Keys outside strings: e.g. {number: or ,slug: or \n title:
            if (c.isJavaIdentifierStart()) {
                val ident = StringBuilder()
                val startIdx = i
                while (i < len && (jsCode[i].isJavaIdentifierPart() || jsCode[i] == '-')) {
                    ident.append(jsCode[i])
                    i++
                }

                // Check what comes after identifier (skipping whitespace)
                var lookAhead = i
                while (lookAhead < len && jsCode[lookAhead].isWhitespace()) {
                    lookAhead++
                }

                val word = ident.toString()
                if (lookAhead < len && jsCode[lookAhead] == ':') {
                    // This is an object key! Emit as JSON quoted key
                    out.append('"').append(word).append('"')
                } else {
                    // Primitive keyword or other identifier (true, false, null, or variable)
                    if (word == "undefined") {
                        out.append("null")
                    } else {
                        out.append(word)
                    }
                }
                continue
            }

            // 3. Skip whitespace or handle structural tokens
            if (c == ',') {
                // Look ahead to check if this is a trailing comma before } or ]
                var lookAhead = i + 1
                while (lookAhead < len && jsCode[lookAhead].isWhitespace()) {
                    lookAhead++
                }
                if (lookAhead < len && (jsCode[lookAhead] == '}' || jsCode[lookAhead] == ']')) {
                    // Skip trailing comma!
                    i++
                    continue
                }
            }

            out.append(c)
            i++
        }

        return out.toString()
    }

    private fun escapeJsonString(s: String): String {
        val sb = StringBuilder("\"")
        for (c in s) {
            when (c) {
                '\\' -> sb.append("\\\\")
                '"' -> sb.append("\\\"")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                else -> {
                    if (c.code in 0..0x1F) {
                        sb.append(String.format("\\u%04x", c.code))
                    } else {
                        sb.append(c)
                    }
                }
            }
        }
        sb.append("\"")
        return sb.toString()
    }
}
