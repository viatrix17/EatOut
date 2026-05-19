package com.example.eatout.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassApiService {

    // Adnotacja @GET doklei "api/interpreter" do adresu bazowego
    @GET("api/interpreter")
    suspend fun getRestaurants(
        @Query("data") query: String
    ): String
}