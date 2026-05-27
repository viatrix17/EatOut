package com.example.eatout.ui.screens

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
import androidx.navigation.NavHostController
import com.example.eatout.GlobalData
import com.example.eatout.data.NoteRepository

@Composable
fun FavouriteScreen(
    isTablet: Boolean,
    navController: NavHostController
) {
    FavouritePhoneLayout()
}

@Composable
fun FavouritePhoneLayout()
{
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = GlobalData.ListOfFavourites, itemContent = { item ->
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