package com.example.eatout.data.model

import com.google.firebase.firestore.DocumentId

data class Note(
    @DocumentId
    var id: String = "",
    var restaurantId: Long = 0L,
    var restauracja: String = "error",
    var lokalizacja: String = "adres",
    var tagi: List<String> = listOf(),
    var lan: Double = 0.0,
    var lon: Double =0.0
)