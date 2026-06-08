package com.example.eatout.data

import org.intellij.lang.annotations.Language

data class Note(
    var restauracja : String = "error",
    var lokalizacja : String = "adres",
    var tagi : Array<String> = arrayOf(),
    var lan : Double= 0.0,
    var lon : Double=0.0
)