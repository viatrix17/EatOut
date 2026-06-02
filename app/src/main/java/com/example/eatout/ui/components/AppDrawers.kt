package com.example.eatout.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment

// TO DO zmniejszyć szerokość drawerów

@Composable
fun AppLeftDrawer(
    navController: NavHostController,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Settings",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Button(
            onClick = {
                navController.navigate("location-settings")
                onCloseDrawer() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Location",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        Button(
            onClick = {
                navController.navigate("mode-settings")
                onCloseDrawer() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Mode",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )

    }
}

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String
)

@Composable
fun AppRightDrawer( // TO DO dodać po prawej stronie
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier,
//    notifications: List<Notification>
) {
    // MOCK DATA

    val notifications = listOf(
        Notification(
            id = 1,
            title = "Potwierdzenie Twojej rezerwacji w lokalu 'Poznańska Pyra'",
            message = "Twoja rezerwacja na dzisiaj, 2 czerwca 2026 roku, na godzinę 19:30 dla 4 osób została pomyślnie potwierdzona przez restaurację. Prosimy o punktualne przybycie, a w razie jakichkolwiek zmian, prosimy o kontakt bezpośrednio z lokalem.",
            time = "10:30"
        ),
        Notification(
            id = 2,
            title = "Specjalna oferta weekendowa tylko dla Ciebie!",
            message = "Z okazji nadchodzącego weekendu przygotowaliśmy dla Ciebie wyjątkowy rabat w wysokości 20% na cały asortyment w naszych partnerskich restauracjach w centrum Poznania. Wystarczy, że przy płatności pokażesz kod QR wygenerowany w aplikacji.",
            time = "12:00"
        ),
        Notification(
            id = 3,
            title = "Ankieta satysfakcji klienta",
            message = "Bardzo cenimy Twoją opinię! Po ostatniej wizycie w 'Restauracji Ratuszowa' chcielibyśmy zapytać, jak oceniasz jakość obsługi oraz smak serwowanych dań. Wypełnienie krótkiej ankiety zajmie Ci zaledwie dwie minuty.",
            time = "14:15"
        )
    )
    ModalDrawerSheet(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Notifications",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(notifications) { notif ->
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.Start) {
                    Text(
                        text = notif.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = notif.message,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = notif.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Opcje filtrowania")
        }
    }
}