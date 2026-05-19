package com.example.eatout.network

import com.example.eatout.GlobalData
import com.example.eatout.model.Post
import java.io.IOException

class ApiService {
    companion object {

        public fun GET(url: String, callback: Callback): Call {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "MojaAplikacjaPoznan/1.0 (kontakt@twojadomena.pl)")
                .build()

            val call = client.newCall(request)
            call.enqueue(callback)
            return call
        }

        fun fetchRestaurantsInPoznan(): String? {
            val client = OkHttpClient()

            // Twój w pełni zakodowany URL z zapytaniem o Poznań
            val url =
                "https://overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Barea%5B%22name%22%3D%22Pozna%C5%84%22%5D-%3E.searchArea%3B%28node%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3Bway%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3Brelation%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B"

            val request = Request.Builder()
                .url(url)
                .get()
                // !!! TO ROZWIĄZUJE PROBLEM BŁĘDU 406 !!!
                .addHeader("User-Agent", "MojaAplikacjaPoznan/1.0 (kontakt@twojadomena.pl)")
                .build()
            var wynik: String = "error 1"
            // Wywołanie asynchroniczne (Enqueue)
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    println("Błąd połączenia: ${e.message}")
                    GlobalData.Companion.restaurants = "blad"
                    GlobalData.Companion.Flag = true
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()?.string()
                        println("Dane pobrane pomyślnie!")
                        // Tutaj masz swój czysty JSON z restauracjami
                        //println(jsonResponse)
                        wynik = jsonResponse.toString()
                        GlobalData.Companion.restaurants = wynik
                        Post.Companion.FindRestaurants(wynik)
                    } else {
                        GlobalData.Companion.restaurants = "blad"
                        // Jeśli zapomnisz o addHeader, wejdzie tutaj z kodem 406
                        println("Błąd serwera");
                        wynik = "error 2"
                        GlobalData.Companion.Flag = true
                    }
                }
            });
            return wynik
        }
    }
}