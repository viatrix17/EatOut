package com.example.eatout.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eatout.data.Note
import com.example.eatout.model.Post
import com.example.eatout.ui.NoteViewModel
import com.example.eatout.viewmodel.RestaurantViewModel

@Composable
fun BrowseScreen(
    isTablet: Boolean,
    navController: NavHostController,
    noteViewModel : NoteViewModel
) {
    BrowsePhoneLayout(noteViewModel)
}

fun addToFavourite(nazwa: String, lokalizacja: String, Tagi: List<String>, lan: Double, lon: Double, noteViewModel: NoteViewModel){
    var note : Note = Note()
    note.restauracja = nazwa;
    note.lokalizacja = lokalizacja
    note.tagi = Tagi.toTypedArray()
    note.lon=lon
    note.lan=lan

    noteViewModel.run {
        addNote(note, "favourites")
    }
}
fun addToVisit(nazwa : String, lokalizacja : String, Tagi : Array<String>, lan : Double, lon : Double, noteViewModel : NoteViewModel){
    var note : Note = Note()
    note.restauracja = nazwa;
    note.lokalizacja = lokalizacja
    note.tagi = Tagi.copyOf()
    note.lon=lon
    note.lan=lan

    noteViewModel.run {
        addNote(note, "rest_to_visit")
    }
}

@Composable
fun BrowsePhoneLayout(noteViewModel : NoteViewModel)
{
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontSize = 20.sp)
    ) {


    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = RestaurantViewModel.allRestaurants, itemContent = { item ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable{addToFavourite(item.name,item.location,Post.returnTag(item.name),item.lan,item.lon,noteViewModel)},
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

        })
    }
        }
}