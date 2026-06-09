package com.example.eatout.util

import kotlin.math.*

object RestaurantProcessor {

    fun returnTags(restaurantName: String): List<String> {
        val tags = listOf("fast", "expensive", "vegan")
        val sum = restaurantName.sumOf { it.code }
        return listOf(tags[sum % 3], tags[((sum % 3) + (sum % 2 + 1)) % 3])
    }

    fun calculateDistance(userLat: Double, userLon: Double, restLat: Double, restLon: Double): Double {
        return sqrt((userLon - restLon).pow(2) + (userLat - restLat).pow(2))
    }
}