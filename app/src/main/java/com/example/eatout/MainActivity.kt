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
        var s : String= ApiService.fetchRestaurantsInPoznan().toString()
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

