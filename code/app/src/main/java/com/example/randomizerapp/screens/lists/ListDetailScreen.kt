package com.example.randomizerapp.screens.lists

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.randomizerapp.common.ResultTile
import com.example.randomizerapp.data.ListItem
import com.example.randomizerapp.data.ListsRepository

private val resultColors = listOf(
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF3F51B5),
    Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFFFF9800)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listId: Long,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: ListsViewModel = viewModel(
        factory = ListsViewModelFactory(ListsRepository(context))
    )

    val lists by viewModel.lists.collectAsState()
    val currentList = lists.find { it.id == listId }

    if (currentList == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Список не найден") }
        return
    }

    var noRepetitions by remember { mutableStateOf(false) }
    var drawnItems by remember { mutableStateOf<List<ListItem>>(emptyList()) }
    var showAddItemDialog by remember { mutableStateOf(false) } // <<<--- ИСПРАВЛЕНО ЗДЕСЬ
    var newItemText by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var resultColor by remember { mutableStateOf(resultColors.first()) }
    val primaryColor = MaterialTheme.colorScheme.primary

    if (showAddItemDialog) {
        AlertDialog(
            onDismissRequest = { showAddItemDialog = false },
            title = { Text("Новый элемент") },
            text = { OutlinedTextField(value = newItemText, onValueChange = { newItemText = it }, label = { Text("Текст") }) },
            confirmButton = { Button(onClick = { if (newItemText.isNotBlank()) { viewModel.addItem(listId, newItemText); newItemText = ""; showAddItemDialog = false } }) { Text("Добавить") } },
            dismissButton = { TextButton(onClick = { showAddItemDialog = false }) { Text("Отмена") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentList.name) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Назад") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val availableItems = if (noRepetitions) currentList.items.filter { it !in drawnItems } else currentList.items
                if (availableItems.isNotEmpty()) {
                    val randomItem = availableItems.random()
                    result = randomItem.text
                    if (noRepetitions) drawnItems = drawnItems + randomItem
                    resultColor = resultColors.random()
                } else if (noRepetitions && currentList.items.isNotEmpty()) {
                    drawnItems = emptyList()
                    result = "Готово!"
                    resultColor = primaryColor
                }
            }) { Icon(Icons.Default.Refresh, "Генерировать") }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(currentList.items, key = { it.id }) { item ->
                        ListItem(
                            headlineContent = { Text(item.text, style = MaterialTheme.typography.headlineSmall) },
                            trailingContent = { IconButton(onClick = { viewModel.deleteItem(listId, item.id) }) { Icon(Icons.Default.Delete, "Удалить") } }
                        )
                        Divider()
                    }
                }
                Button(onClick = { showAddItemDialog = true }, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Add, null, Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Добавить элемент")
                }
                Card(modifier = Modifier.padding(bottom = 24.dp), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Без повторений")
                            Text("${drawnItems.size}/${currentList.items.size}", style = MaterialTheme.typography.headlineSmall)
                        }
                        Spacer(Modifier.width(16.dp))
                        Switch(checked = noRepetitions, onCheckedChange = { noRepetitions = it; if (!it) drawnItems = emptyList() })
                    }
                }
            }
            AnimatedVisibility(visible = result != null, enter = scaleIn(), exit = scaleOut()) {
                ResultTile(text = result ?: "", color = resultColor, onClick = { result = null })
            }
        }
    }
}