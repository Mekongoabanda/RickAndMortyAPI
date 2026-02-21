package com.example.rickandmortyapi.service

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//call interface
interface CharacterService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,

    ): String
}

object CharacterApi{
    val retrofitService: CharacterService by lazy {
        retrofit.create(CharacterService::class.java)
    }
}
