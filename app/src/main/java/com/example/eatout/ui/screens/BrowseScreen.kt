package com.example.eatout.ui.screens

import android.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eatout.GlobalData

@Composable
fun BrowseScreen(
    isTablet: Boolean,
    navController: NavHostController
) {
    BrowsePhoneLayout()
}

fun addToFavourite(string : String){
    GlobalData.ListOfFavourites.add(string)
}

@Composable
fun BrowsePhoneLayout()
{

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = GlobalData.ListOfRestaurants, itemContent = { item ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable{addToFavourite(item)},
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

        })
    }
}