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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.eatout.GlobalData
import com.example.eatout.data.Note
import com.example.eatout.data.NoteRepository
import com.example.eatout.ui.NoteViewModel

@Composable
fun FavouriteScreen(
    isTablet: Boolean,
    navController: NavHostController,
    noteViewModel: NoteViewModel
) {
    FavouritePhoneLayout(noteViewModel)
}

@Composable
fun FavouritePhoneLayout(noteViewModel: NoteViewModel)
{
    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        GlobalData.ListOfFavourites = noteViewModel.getNotes() as ArrayList<Note>;
    }
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = GlobalData.ListOfFavourites, itemContent = { item ->
            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.restauracja,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

        })
    }
}