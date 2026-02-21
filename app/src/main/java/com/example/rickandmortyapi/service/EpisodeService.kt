package com.example.rickandmortyapi.service

import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeService{
    @GET("episode")
    suspend fun getEpisodes(
        @Query("id") id: Int? = null,
        @Query("name") name: String? = null,
        @Query("type") type: Int? = null,
        @Query("dimensions") episode: Int? = null
    ): String
}

object EpisodeApi {
    val service: EpisodeService by lazy {
        retrofit.create(EpisodeService::class.java)
    }
}