//ApiService.kt
package com.example.simplerecipesearchapp

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface ApiService {
    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealResponse
}

object ApiClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    //from lecture example
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Explicitly add KotlinJsonAdapterFactory
        .build() // Build the Moshi instance

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
