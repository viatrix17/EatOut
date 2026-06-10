package com.example.eatout.domain.model

data class Restaurant(
    val id: Long,
    val name: String,
    val address: String,
    val cuisineType: String = "Restaurant",
    val tags: List<String> = emptyList(),
    val isToVisit: Boolean = false,
    val isFavorite: Boolean = false,
    var lan: Double = 0.0,
    var lon: Double = 0.0
)

data class RestaurantUIState(
    val id: Long,
    val name: String,
    val address: String,
    val cuisineType: String,
    val tags: List<String>,
    var lan: Double = 0.0,
    var lon: Double = 0.0,
    // Stany z Firebase
    val isToVisit: Boolean,
    val isFavorite: Boolean
)