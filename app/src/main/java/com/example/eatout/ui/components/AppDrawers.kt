package com.example.eatout.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eatout.viewmodel.RestaurantViewModel
import com.example.eatout.viewmodel.SortOption
import com.example.eatout.viewmodel.SortOrder
import kotlinx.coroutines.flow.forEach

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
    val time: String,
    val isRead: Boolean = false
)

@Composable
fun AppRightDrawer(
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier,
//    notifications: List<Notification>
) {
    // MOCK DATA

    val notifications = remember {
        mutableStateListOf(
            Notification(
                id = 1,
                title = "Potwierdzenie Twojej rezerwacji w lokalu 'Poznańska Pyra'",
                message = "Twoja rezerwacja na dzisiaj, 2 czerwca 2026 roku, na godzinę 19:30 dla 4 osób została pomyślnie potwierdzona przez restaurację.",
                time = "10:30"
            ),
            Notification(
                id = 2,
                title = "Specjalna oferta weekendowa tylko dla Ciebie!",
                message = "Z okazji nadchodzącego weekendu przygotowaliśmy dla Ciebie wyjątkowy rabat w wysokości 20%.",
                time = "12:00"
            ),
            Notification(
                id = 3,
                title = "Ankieta satysfakcji klienta",
                message = "Bardzo cenimy Twoją opinię! Po ostatniej wizycie w 'Restauracji Ratuszowa' chcielibyśmy zapytać, jak oceniasz obsługę.",
                time = "14:15"
            )
        )
    }

    var selectedNotification by remember { mutableStateOf<Notification?>(null) }

    selectedNotification?.let { notif ->
        CustomAlertDialog(
            onDismissRequest = { selectedNotification = null },
            title = notif.title,
            message = notif.message
        )
    }

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
                Surface(
                    onClick = {
                        val index = notifications.indexOf(notif)
                        if (index != -1) {
                            notifications.set(index, notif.copy(isRead = true))
                        }
                        selectedNotification = notif
                    },
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = notif.title,
                            fontWeight = if (notif.isRead) FontWeight.Normal else FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = notif.message,
                            fontWeight = if (notif.isRead) FontWeight.Normal else FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = notif.time,
                            fontWeight = if (notif.isRead) FontWeight.Normal else FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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
    onDismiss: () -> Unit,
    viewModel: RestaurantViewModel = viewModel()
) {

    val labels by viewModel.allLabels.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val currentSort by viewModel.currentSort.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Text("Filter options",
                style = MaterialTheme.typography.bodyLarge
            )
            Row {
                Column(modifier = Modifier.weight(2f)) {
                    FilterDropdown(
                        label = "Category",
                        options = labels,
                        selectedOptions = selectedTags,
                        onOptionToggled = { tag -> viewModel.toggleTag(tag) }
                    )

                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Sort")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleSortOrder() }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (sortOrder == SortOrder.ASC) "From A to Z" else "From Z to A",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Icon(
                            imageVector = if (sortOrder == SortOrder.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = "Change sort order"
                        )
                    }
                }
            }
        }
    }
}