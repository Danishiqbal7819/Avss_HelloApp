package com.example.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class StateRequest(
    val country: String,
    val state: String
)

data class CityResponse(
    val msg: String,
    val data: List<String>
)

interface CountryApiService {
    @POST("countries/state/cities")
    fun getCities(@Body request: StateRequest): Call<CityResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://countriesnow.space/api/v0.1/"
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}