package com.example.eatout.util

import kotlin.math.*

object RestaurantProcessor {

    fun returnTags(restaurantName: String): List<String> {
        val tags = listOf("fast", "expensive", "vegan")
        val sum = restaurantName.sumOf { it.code }
        return listOf(tags[sum % 3], tags[((sum % 3) + (sum % 2 + 1)) % 3])
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }
}