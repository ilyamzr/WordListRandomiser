package com.example.randomizerapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.randomizerapp.ui.theme.RandomizerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рандом") },
                actions = {
                    IconButton(onClick = { /* TODO: Обработка настроек */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                    IconButton(onClick = { /* TODO: Обработка дополнительного меню */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Еще")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    RandomizerCard(
                        icon = Icons.Default.Looks3, // Или другой значок для числа
                        title = "ЧИСЛО",
                        onClick = { navController.navigate("number_screen") }
                    )
                }
                item {
                    RandomizerCard(
                        icon = Icons.Default.List,
                        title = "СПИСОК",
                        onClick = { navController.navigate("lists_management_screen") }
                    )
                }
                item {
                    RandomizerCard(
                        icon = Icons.Default.Casino, // Или Icons.Default.DataObject для кубиков
                        title = "КУБИКИ",
                        onClick = { navController.navigate("dice_screen") }
                    )
                }
                item {
                    RandomizerCard(
                        icon = Icons.Default.Check, // Или другой значок для жребия
                        title = "ЖРЕБИЙ",
                        onClick = { navController.navigate("draw_screen") }
                    )
                }
                item {
                    RandomizerCard(
                        icon = Icons.Default.AttachMoney, // Или другой значок для монетки
                        title = "МОНЕТКА",
                        onClick = { navController.navigate("coin_screen") }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomizerCard(icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
            // Убрали horizontalArrangement, чтобы выровнять по левому краю
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Иконка "+" была здесь и была удалена
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    RandomizerAppTheme {
        MainScreen(rememberNavController())
    }
}