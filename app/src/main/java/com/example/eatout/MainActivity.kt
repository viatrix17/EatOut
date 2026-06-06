package com.example.eatout

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.example.eatout.network.ApiService
import com.example.eatout.ui.components.CustomTopBar
import com.example.eatout.ui.theme.EatOutTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eatout.data.Note
import com.example.eatout.ui.NoteViewModel
import com.example.eatout.ui.components.CustomBottomBar
import com.example.eatout.ui.navigation.AppNavHost
import com.example.eatout.viewmodel.MainViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlin.properties.Delegates

class GlobalData {
    companion object {
        var restaurants = "error"
        var Flag = false
        var ListOfRestaurants: ArrayList<String> = arrayListOf()
        var ListOfFavourites: ArrayList<Note> = arrayListOf()
        var RestaurantsLontitudes : ArrayList<String> = arrayListOf()
        var RestaurantsLantitudes : ArrayList<String> = arrayListOf()
        var Distances : ArrayList<Double> = arrayListOf()
        var ListOfRestaurantsToVisit: ArrayList<Note> = arrayListOf()

        var ListOfDishes: ArrayList<Note> = arrayListOf()

        var ListOfFavouriteDishes: ArrayList<Note> = arrayListOf()

        lateinit var fusedLocationClient: FusedLocationProviderClient
        var latitude : Double = 0.0
        var longitude : Double = 0.0
    }
}


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    @androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
           arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )

        GlobalData.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        GlobalData.fusedLocationClient.getCurrentLocation( Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location : Location? ->
                if(location != null) {
                    GlobalData.latitude = location?.latitude!!
                    GlobalData.longitude = location?.longitude!!
                    Log.d("TAG", "got location")
                    Log.d("TAG", GlobalData.latitude.toString())
                    Log.d("TAG", GlobalData.longitude.toString())
                }else{
                    Log.d("TAG", "got null")
                }
            }
        enableEdgeToEdge()
        setContent  {

                val topBarTitle = "EatOut"
                val isTablet = false
                // configuration.smallestScreenWidthDp >= 600

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val isLoading by viewModel.isLoading.collectAsState()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            val isHome = currentDestination?.route == "home"
                            val isNotReady = currentDestination == null
                            CustomTopBar(
                                title = topBarTitle,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                showBackButton = !isHome && !isNotReady,
                                modifier = Modifier
                            )
                        },
                        bottomBar = {
                            CustomBottomBar(
                                navController = navController
                            )
                        }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            isLoading = isLoading,
                            isTablet = isTablet,
                            noteViewModel = NoteViewModel(),
                            modifier = Modifier.padding(innerPadding)

                        )
                    }
                }
            }
        }
    }


@Composable
fun Greeting(textToShow: String, modifier: Modifier = Modifier) {
    Text(
        text = textToShow,
        modifier = modifier
    )
}