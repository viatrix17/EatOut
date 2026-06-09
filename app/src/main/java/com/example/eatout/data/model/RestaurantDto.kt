package com.example.eatout.data.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class RestaurantDto(
    val id: Long,
    val lat: Double,
    val lon: Double,
    @SerializedName("tags")
    val apiTags: TagsDto?
) {
    val name: String
        get() = apiTags?.name ?: "Unknown restaurant"
}

data class TagsDto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("addr:street") val street: String?,
    @SerializedName("addr:housenumber") val houseNumber: String?,
    @SerializedName("addr:postcode") val postcode: String?,
    @SerializedName("addr:city") val city: String?
) {
    init {
        Log.d("DEBUG_TAGS", "TagsDto utworzone, name: $name")
    }
    fun getFullAddress(): String {
        val streetPart = listOfNotNull(street, houseNumber).joinToString(" ")
        val cityPart = listOfNotNull(postcode, city).joinToString(" ")

        return listOfNotNull(streetPart.ifBlank { null }, cityPart.ifBlank { null })
            .joinToString(", ")
            .ifBlank { "Brak adresu" }
    }
}