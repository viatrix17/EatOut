package com.example.eatout.data.repository

import android.util.Log
import com.example.eatout.data.model.OverpassResponse
import com.example.eatout.domain.model.Restaurant
import com.example.eatout.network.OverpassApiService
import com.example.eatout.util.RestaurantProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class RestaurantRepository(private val apiService: OverpassApiService) {

    private val _restaurantsFlow = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurantsFlow = _restaurantsFlow.asStateFlow()

    suspend fun fetchRestaurants(): List<Restaurant>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRestaurants(getPoznanQuery())
                Log.d("DEBUG_REPO", "Otrzymano odpowiedź z Overpass: $response")
                if (response == null) {
//                    Log.e("DEBUG_REPO", "Odpowiedź jest NULL!")
                    return@withContext emptyList()
                }
                response?.elements?.forEachIndexed { index, dto ->
//                    Log.d("DEBUG_TAGS", "Element $index: tags is null? ${dto.apiTags == null}")
                }


                val result = response?.elements?.map { dto ->
                    val name = dto.apiTags?.name ?: "Unknown restaurant"
                    val generatedTags = RestaurantProcessor.returnTags(name)
                    Restaurant(
                        id = dto.id,
                        name = dto.apiTags?.name ?: "Unknown restaurant",
                        lan = dto.lat,
                        lon = dto.lon,
                        address = dto.apiTags?.getFullAddress() ?: "Unknown address",
                        tags = generatedTags,
                        isFavorite = false,
                        isToVisit = false
                    )
                } ?: emptyList()
                Log.d("DEBUG_REPO", "Przetworzono ${result.size} elementów")
                _restaurantsFlow.value = result
                result
            } catch (e: Exception) {
                Log.e("DEBUG_REPO", "Błąd wywołania API: ", e)
                null
            }
        }
    }

    private fun getPoznanQuery() = """
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
}