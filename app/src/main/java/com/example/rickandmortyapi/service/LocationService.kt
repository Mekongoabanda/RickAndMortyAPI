package com.example.rickandmortyapi.service

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService{
    @GET("location")
    suspend fun getLocations(
        @Query("id") id: Int? = null,
        @Query("name") name: String? = null,
        @Query("type") planet: String? = null,
        @Query("dimension") dimension: String? = null
    ): String
}

object LocationApi{
    val service : LocationService by lazy{
        retrofit.create(LocationService::class.java)
    }
}