package com.example.eatout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.eatout.ui.components.CustomTopBar
import com.example.eatout.ui.theme.EatOutTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eatout.ui.components.AppLeftDrawer
import com.example.eatout.ui.components.AppRightDrawer
import com.example.eatout.ui.components.CustomBottomBar
import com.example.eatout.ui.navigation.AppNavHost
import com.example.eatout.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class GlobalData {
    companion object {
        var restaurants = "error"
        var Flag = false
        var ListOfRestaurants: ArrayList<String> = arrayListOf()
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemTheme by viewModel.isSystemTheme.collectAsStateWithLifecycle()
            val isDarkThemeCustom by viewModel.isDarkThemeCustom.collectAsStateWithLifecycle()

            val isSystemDark = isSystemInDarkTheme()

            val useDarkTheme = when {
                isSystemTheme == null || isDarkThemeCustom == null -> isSystemInDarkTheme() // Czekaj na wczytanie
                isSystemTheme == true -> isSystemInDarkTheme()
                else -> isDarkThemeCustom == true
            }

            EatOutTheme(darkTheme = useDarkTheme) {
                val topBarTitle = "EatOut"
//               val configuration = LocalConfiguration.current
                val isTablet = false// configuration.smallestScreenWidthDp >= 600

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val isLoading by viewModel.isLoading.collectAsState()

                val scope = rememberCoroutineScope()

                val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val rightDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                val configuration = androidx.compose.ui.platform.LocalConfiguration.current
                val halfScreenWidth = (configuration.screenWidthDp / 2).dp
                val drawerModifier = Modifier.widthIn(max = halfScreenWidth)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModalNavigationDrawer(
                        drawerState = leftDrawerState,
                        drawerContent = {
                            AppLeftDrawer(
                                navController = navController,
                                onCloseDrawer = { scope.launch { leftDrawerState.close() } },
                                modifier = drawerModifier
                            )
                        }
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            ModalNavigationDrawer(
                                drawerState = rightDrawerState,
                                drawerContent = {
                                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                        AppRightDrawer(
                                            onCloseDrawer = { scope.launch { rightDrawerState.close() } },
                                            modifier = drawerModifier
                                        )
                                    }
                                }
                            ) {
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                    Scaffold(
                                        modifier = Modifier.fillMaxSize(),
                                        topBar = {
                                            val isHome = currentDestination?.route == "home"
                                            val isNotReady = currentDestination == null
                                            CustomTopBar(
                                                title = topBarTitle,
                                                onMenuClick = {
                                                    scope.launch {
                                                        leftDrawerState.open()
                                                    }
                                                },
                                                onNotifClick = {
                                                    scope.launch {
                                                        rightDrawerState.open()
                                                    }
                                                },
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
                                            modifier = Modifier.padding(innerPadding)
                                        )
                                    }
                                }
                            }
                        }
                    }
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