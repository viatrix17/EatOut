package com.example.eatout.network

import com.example.eatout.GlobalData
import com.example.eatout.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiService {
    companion object {

        suspend fun fetchRestaurantsInPoznan() {

            val overpassQuery = """
                [out:json];
                area["name"="Poznań"]->.searchArea;
                (
                  node["amenity"="restaurant"](area.searchArea);
                  way["amenity"="restaurant"](area.searchArea);
                  relation["amenity"="restaurant"](area.searchArea);
                );
                out body;
                >;
                out skel qt;
            """.trimIndent()

            withContext(Dispatchers.IO) {
                try {
                    val jsonResponse = RetrofitInstance.apiService.getRestaurants(overpassQuery)

                    println("Dane pobrane pomyślnie przez Retrofit!")

                    GlobalData.Companion.restaurants = jsonResponse
                    Post.Companion.FindRestaurants(jsonResponse)

                } catch (e: Exception) {
                    println("Błąd pobierania danych Retrofit: ${e.message}")
                    GlobalData.Companion.restaurants = "blad"
                    GlobalData.Companion.Flag = true
                }
            }
        }
    }
}