package com.example.eatout.data
data class Note(
    var restauracja : String = "error",
    var distance : Double = 0.0,
    var favourite : Boolean = false,
    var toVisit : Boolean = false
)