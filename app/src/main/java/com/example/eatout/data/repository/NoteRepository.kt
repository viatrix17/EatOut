package com.example.eatout.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.eatout.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

// Klasa repozytorium odpowiedzialna za komunikację z bazą Firestore
// Klasa repozytorium odpowiedzialna za komunikację z bazą Firestore
class NoteRepository {
    // Pobranie instancji bazy danych Firestore
    private val db = FirebaseFirestore.getInstance()
    private var nextId = 0;
    // Odwołanie do kolekcji "notes" w bazie Firestore
    // Funkcja dodająca nową notatkę do bazy; suspend oznacza, że funkcja działa asynchronicznie w korutynie
    suspend fun addNote(note: Note, collection : String) {
        var notatki = getNotes(collection)

// Utworzenie nowego dokumentu z automatycznie generowanym ID
        val docRef = db.collection(collection).document()
// Skopiowanie obiektu note i przypisanie mu wygenerowanego ID dokumentu
        val noteWithId = note.copy(restauracja = note.restauracja)
        nextId+=1
        if(note in notatki){
            return
        }else{
            docRef.set(noteWithId).await()
        }
// Zapisanie notatki do Firestore; await() wstrzymuje wykonanie korutyny do momentu zakończenia operacji

    }
    // Funkcja pobierająca wszystkie notatki z kolekcji "notes"
    suspend fun getNotes(collection : String): List<Note> {
// Pobranie wszystkich dokumentów z kolekcji
        val snapshot = db.collection(collection).get().await()
// Zamiana dokumentów Firestore na obiekty klasy Note; mapNotNull pomija ewentualne błędne lub puste wyniki
        return snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
    }
    suspend fun clearNotes(collection : String){
        val docRef = db.collection(collection).document()
        val x = db.collection(collection).get()
        x.addOnSuccessListener {
            for (document in it) {
                db.collection(collection).document(document.id).delete()
            }
        }
        Log.d(ContentValues.TAG,x.toString())
    }
    fun observeNotes(collection : String,
                     onDataChanged: (List<Note>) -> Unit, onError: (Exception) -> Unit): ListenerRegistration {
// Dodanie listenera nasłuchującego zmiany w kolekcji "notes"
        return db.collection(collection).addSnapshotListener { snapshot, error ->
// Sprawdzenie, czy podczas pobierania danych wystąpił błąd
            if (error != null) {
                onError(error)
// Zakończenie działania listenera dla tego wywołania
                return@addSnapshotListener
            }
// Zamiana dokumentów z Firestore na listę obiektów klasy Note
// snapshot?.documents - lista dokumentów pobranych z kolekcji
// mapNotNull - przekształca dokumenty na obiekty Note i pomija wartości null
            val notes = snapshot?.documents?.mapNotNull { document -> document.toObject(Note::class.java)} ?: emptyList()
// Przekazanie aktualnej listy notatek do funkcji obsługującej dane
            onDataChanged(notes)
        }
    }
    suspend fun addNote(collection: String, note: Note) {
        db.collection(collection).document().set(note).await()
    }

    suspend fun removeNote(collection: String, restaurantName: String) {
        val snapshot = db.collection(collection)
            .whereEqualTo("restauracja", restaurantName)
            .get()
            .await()
        for (document in snapshot.documents) {
            document.reference.delete().await()
        }
    }

    suspend fun toggleNote(collection: String, restaurantName: String, note: Note) {
        val snapshot = db.collection(collection)
            .whereEqualTo("restauracja", restaurantName)
            .get()
            .await()

        if (snapshot.isEmpty) {
            db.collection(collection).document().set(note).await()
        } else {
            // Jeśli jest na liście – usuwamy
            for (document in snapshot.documents) {
                document.reference.delete().await()
            }
        }
    }
}