package com.example.eatout.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.eatout.data.repository.RestaurantRepository

val LocalRepository = staticCompositionLocalOf<RestaurantRepository> {
    error("RestaurantRepository was not provided in CompositionLocalProvider")
}