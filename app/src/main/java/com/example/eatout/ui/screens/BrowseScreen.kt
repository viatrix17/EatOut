package com.example.eatout.ui.screens

import android.graphics.Color
import androidx.compose.foundation.border
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eatout.GlobalData
import com.example.eatout.data.Note
import com.example.eatout.ui.NoteViewModel

@Composable
fun BrowseScreen(
    isTablet: Boolean,
    navController: NavHostController,
    noteViewModel : NoteViewModel
) {
    BrowsePhoneLayout(noteViewModel)
}

fun addToFavourite(string : String, noteViewModel : NoteViewModel){
    var note : Note = Note()
    note.restauracja = string;

    noteViewModel.run {
        addNote(note, "favourites")
    }
}

@Composable
fun BrowsePhoneLayout(noteViewModel : NoteViewModel)
{
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontSize = 20.sp)
    ) {

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = GlobalData.ListOfRestaurants, itemContent = { item ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable{addToFavourite(item,noteViewModel)},
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
}