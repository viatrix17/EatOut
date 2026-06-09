package com.example.eatout.domain.model

data class Dish(
    val id: Int,
    val restaurantId: Long,
    val name: String,
    val price: Double,
    val ingredients: String,
    val labels: List<String>,
    val isFavorite: Boolean,
    val isToTry: Boolean
)
