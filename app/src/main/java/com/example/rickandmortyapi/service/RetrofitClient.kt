package com.example.rickandmortyapi.service

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val BASE_URL = "https://rickandmortyapi.com/api/"
var scalarsConverterFactory = ScalarsConverterFactory.create()

//build retrofit
val retrofit = Retrofit.Builder()
    .addConverterFactory(scalarsConverterFactory)
    .baseUrl(BASE_URL)
    .build()
