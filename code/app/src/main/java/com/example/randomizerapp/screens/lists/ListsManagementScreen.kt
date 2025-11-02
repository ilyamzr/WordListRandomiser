package com.example.randomizerapp.screens.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.randomizerapp.data.ListsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsManagementScreen(
    navController: NavController
) {
    // Создаем ViewModel вручную
    val context = LocalContext.current
    val viewModel: ListsViewModel = viewModel(
        factory = ListsViewModelFactory(ListsRepository(context))
    )

    val lists by viewModel.lists.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Новый список") },
            text = { OutlinedTextField(value = newListName, onValueChange = { newListName = it }, label = { Text("Название списка") }) },
            confirmButton = { Button(onClick = { if (newListName.isNotBlank()) { viewModel.addList(newListName); newListName = ""; showAddDialog = false } }) { Text("Создать") } },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Отмена") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ваши списки") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Назад") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, "Добавить список") }
        }
    ) { padding ->
        if (lists.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("У вас пока нет списков") }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(lists, key = { it.id }) { list ->
                    ListItem(
                        headlineContent = { Text(list.name) },
                        supportingContent = { Text("${list.items.size} элементов") },
                        modifier = Modifier.clickable { navController.navigate("list_detail_screen/${list.id}") },
                        trailingContent = { IconButton(onClick = { viewModel.deleteList(list.id) }) { Icon(Icons.Default.Delete, "Удалить") } }
                    )
                    Divider()
                }
            }
        }
    }
}