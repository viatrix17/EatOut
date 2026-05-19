package com.example.eatout

import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eatout.ui.theme.EatOutTheme
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Thread.sleep
import kotlin.math.min

class GlobalData {
    companion object {
        var restaurants= "error"
        var Flag = false
        var ListOfRestaurants : ArrayList<String> = arrayListOf<String>()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatOutTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        application = application
                    )
                }
            }
        }
        var s : String=fetchRestaurantsInPoznan().toString()
        while(GlobalData.Flag==false)
        {
            sleep(100)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, application : Application) {
    Text(
        text = GlobalData.ListOfRestaurants.toString(),
        modifier = modifier
    )
}
fun ignoreCaseOpt(ignoreCase: Boolean) =
    if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
    pat.toRegex(ignoreCaseOpt(ignoreCase))
        .findAll(this?: "")
        .map { it.range.first }
        .toList()
fun FindRestaurants(string : String){
    var result = ArrayList<String>()
    var indexes = string.indexesOf("\"name\"", false)
    for(i in indexes){
        var r = ""
        var j = i + 1
        //while(string[j] != '"'){
        //    r += string[j]
        //}
        var a = string.substring(i+9,min(i+100, string.length)).indexesOf("\"", false).first()
        println(a)
        if(a > 0 && a + i + 9 < string.length) {
            r = string.substring(i + 9, i + 9 + a)
            println(r)
            result.add(r)
        }
    }
    println(result.toString())
    GlobalData.ListOfRestaurants = result
    GlobalData.Flag = true
}
fun fetchRestaurantsInPoznan() : String?{
    val client = OkHttpClient()

    // Twój w pełni zakodowany URL z zapytaniem o Poznań
    val url = "https://overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Barea%5B%22name%22%3D%22Pozna%C5%84%22%5D-%3E.searchArea%3B%28node%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3Bway%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3Brelation%5B%22amenity%22%3D%22restaurant%22%5D%28area.searchArea%29%3B%29%3Bout%20body%3B%3E%3Bout%20skel%20qt%3B"

    val request = Request.Builder()
        .url(url)
        .get()
        // !!! TO ROZWIĄZUJE PROBLEM BŁĘDU 406 !!!
        .addHeader("User-Agent", "MojaAplikacjaPoznan/1.0 (kontakt@twojadomena.pl)")
        .build()
    var wynik : String = "error 1"
    // Wywołanie asynchroniczne (Enqueue)
    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            println("Błąd połączenia: ${e.message}")
            GlobalData.restaurants = "blad"
            GlobalData.Flag = true
        }
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                val jsonResponse = response.body()?.string()
                println("Dane pobrane pomyślnie!")
                // Tutaj masz swój czysty JSON z restauracjami
                //println(jsonResponse)
                wynik = jsonResponse.toString()
                GlobalData.restaurants = wynik
                FindRestaurants(wynik)
            } else {
                GlobalData.restaurants = "blad"
                // Jeśli zapomnisz o addHeader, wejdzie tutaj z kodem 406
                println("Błąd serwera");
                wynik = "error 2"
                GlobalData.Flag = true
            }
        }
    });
    return wynik
}