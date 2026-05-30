package com.example.eatout.ui
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatout.data.Note
import com.example.eatout.data.NoteRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    private val repository = NoteRepository()
    // Obiekt przechowujący aktywny listener Firestore, dzięki niemu można później zatrzymać nasłuchiwanie zmian
    private var listenerRegistration: ListenerRegistration? = null
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    // Funkcja uruchamiająca synchronizację danych z Firestore
    fun startSync(collection : String) {
// Rozpoczęcie obserwacji zmian w kolekcji "notes"
        listenerRegistration = repository.observeNotes(
// Funkcja wywoływana po poprawnym pobraniu lub zmianie danych
            onDataChanged = { notes ->_notes.value = notes},// Aktualizacja stanu ViewModelu nową listą notatek },
// Funkcja wywoływana w przypadku błędu
                onError = { e ->e.printStackTrace()} // Wypisanie błędu w logach
                , collection = collection)
            }
    fun addNote(note: Note, collection : String) {
        viewModelScope.launch {
            repository.addNote(note, collection)
            Log.d("TAG", note.toString())
            _notes.value = repository.getNotes(collection) // Po dodaniu notatki ponownie pobieramy listę danych
            Log.d("TAG",notes.value.last().toString())
        }
    } // Funkcja dodająca nową notatkę do bazy – nic się tu nie zmienia
    fun clearNotes(collection: String){
        viewModelScope.launch {
            repository.clearNotes(collection)
            //Log.d("TAG", notes.value.last().toString())
            _notes.value = repository.getNotes(collection) // Po dodaniu notatki ponownie pobieramy listę danych
            //Log.d("TAG",notes.value.last().toString())
        }
    }
    suspend fun getNotes(collection: String): List<Note> {
        return repository.getNotes(collection);
    }
// Funkcja wywoływana automatycznie przy usuwaniu ViewModelu
                    override fun onCleared() {
                super.onCleared()
// Usunięcie listenera Firestore, aby zatrzymać nasłuchiwanie zmian i uniknąć niepotrzebnego zużycia zasobów
                listenerRegistration?.remove()
            }
    }
