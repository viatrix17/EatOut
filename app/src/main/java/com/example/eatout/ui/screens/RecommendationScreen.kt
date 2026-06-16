    package com.example.eatout.ui.screens
    import android.util.Log
    import androidx.compose.animation.AnimatedContent
    import androidx.compose.animation.SizeTransform
    import androidx.compose.animation.core.tween
    import androidx.compose.animation.fadeIn
    import androidx.compose.animation.fadeOut
    import androidx.compose.animation.slideInVertically
    import androidx.compose.animation.slideOutVertically
    import androidx.compose.animation.togetherWith
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.material3.Button
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.navigation.NavHostController
    import com.example.eatout.domain.model.RestaurantUIState
    import com.example.eatout.viewmodel.RestaurantViewModel
    import com.example.eatout.R
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Explore
    import androidx.compose.material.icons.filled.RestaurantMenu
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material3.*
    import androidx.compose.ui.unit.dp
    import com.airbnb.lottie.compose.LottieAnimation
    import com.airbnb.lottie.compose.LottieCompositionSpec
    import com.airbnb.lottie.compose.animateLottieCompositionAsState
    import com.airbnb.lottie.compose.rememberLottieComposition

    @Composable
    fun RecommendationScreen(
        isTablet: Boolean,
        navController: NavHostController,
        viewModel: RestaurantViewModel
    ) {
        val restaurant by viewModel.dailyRecommendation.collectAsState()
        val showCelebration by viewModel.shouldShowCelebration.collectAsState()

        AnimatedContent(
            targetState = restaurant,
            transitionSpec = {
                (slideInVertically(initialOffsetY = { 300 }) + fadeIn(animationSpec = tween(700)))
                    .togetherWith(
                        slideOutVertically(targetOffsetY = { -300 }) + fadeOut(animationSpec = tween(700))
                    )
                    .using(SizeTransform(clip = false))
            },
            label = "FancyRecommendationTransition"
        ) { targetRestaurant ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (targetRestaurant == null) {
                    RecommendationEmptyLayout(
                        onGenerateClick = { viewModel.triggerDailyRecommendation() }
                    )
                } else {
                    if (showCelebration) {
                        CelebrationAnimation()
                    }

                    RecommendationPhoneLayout(
                        restaurant = targetRestaurant,
                        onRestaurantClick = { id ->
                            navController.navigate("details/$id")
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun RecommendationEmptyLayout(onGenerateClick: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = null,
                modifier = Modifier.size(150.dp).padding(bottom = 24.dp)
            )
//            Text(
//                text = "Brak rekomendacji na dzisiaj",
//                style = MaterialTheme.typography.headlineSmall,
//                textAlign = TextAlign.Center
//            )
            Text(
                text = "Tap below to discover a restaurant worth visiting!!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )
            Button(
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Wylosuj restaurację")
            }
        }
    }

    @Composable
    fun RecommendationPhoneLayout(
        restaurant: RestaurantUIState,
        onRestaurantClick: (Long) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Recommendation for today:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onRestaurantClick(restaurant.id) },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = restaurant.address,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Cuisine: ${restaurant.cuisineType}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                }


            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tap the card to see details",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        }
    }

    @Composable
    fun CelebrationAnimation() {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))

        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 1
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
    }