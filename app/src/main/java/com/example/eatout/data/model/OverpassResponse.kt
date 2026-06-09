package com.example.eatout.data.model

import com.example.eatout.data.model.RestaurantDto
import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("elements") val elements: List<RestaurantDto>? = emptyList()
)