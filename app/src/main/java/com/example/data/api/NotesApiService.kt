package com.example.data.api

import com.example.data.model.NotesData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NotesApiService {

    /**
     * Fetch the root HTML page to discover the latest Vite index bundle script.
     */
    @GET("/")
    suspend fun getIndexHtml(): ResponseBody

    /**
     * Fetch the dynamic JS bundle from the parsed URL.
     */
    @GET
    suspend fun getJsBundle(@Url url: String): ResponseBody

    /**
     * Fetch JSON directly if the server hosts notes_data.json or api/notes.json.
     */
    @GET("notes_data.json")
    suspend fun getNotesDataDirect(): Response<NotesData>
}
