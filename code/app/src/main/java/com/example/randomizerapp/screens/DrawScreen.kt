package com.example.randomizerapp.screens
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.randomizerapp.ui.theme.RandomizerAppTheme

private data class FieldItem(
    val id: Int,
    val color: Color,
    val isMarked: Boolean,
    var isRevealed: Boolean = false
)
private enum class DrawGameState {
    SETTINGS,
    GRID,
    RESULT
}
private val fieldColors = listOf(
    Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
    Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722)
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(navController: NavController) {
    var gameState by remember { mutableStateOf(DrawGameState.SETTINGS) }
    var totalFields by remember { mutableStateOf("20") }
    var markedFields by remember { mutableStateOf("1") }
    var fields by remember { mutableStateOf<List<FieldItem>>(emptyList()) }
    fun generateFields() {
        val total = totalFields.toIntOrNull() ?: 0
        val marked = markedFields.toIntOrNull() ?: 0
        if (total > 0 && marked > 0 && marked <= total) {
            val markedIndices = (0 until total).shuffled().take(marked).toSet()
            fields = List(total) { index ->
                FieldItem(
                    id = index,
                    color = fieldColors.random(),
                    isMarked = index in markedIndices
                )
            }
            gameState = DrawGameState.GRID
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Жребий") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (gameState == DrawGameState.SETTINGS) {
                            navController.popBackStack()
                        } else {
                            // Если игра началась, кнопка "назад" сбрасывает ее
                            gameState = DrawGameState.SETTINGS
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                when (gameState) {
                    DrawGameState.SETTINGS -> generateFields()
                    // Кнопка "Refresh" теперь сбрасывает игру
                    DrawGameState.GRID, DrawGameState.RESULT -> generateFields()
                }
            }) {
                val icon = when (gameState) {
                    DrawGameState.SETTINGS -> Icons.Default.Check
                    else -> Icons.Default.Refresh
                }
                Icon(icon, contentDescription = "Действие")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (gameState) {
                DrawGameState.SETTINGS -> SettingsView(
                    totalFields = totalFields,
                    onTotalChange = { totalFields = it },
                    markedFields = markedFields,
                    onMarkedChange = { markedFields = it }
                )
                DrawGameState.GRID, DrawGameState.RESULT -> GridView(
                    fields = fields,
                    gameState = gameState,
                    onFieldClick = { index ->
                        // ИЗМЕНЕННАЯ ЛОГИКА
                        if (gameState == DrawGameState.GRID) {
                            val updatedFields = fields.toMutableList()
                            val clickedField = updatedFields[index]

                            // 1. Открываем нажатую плитку
                            updatedFields[index] = clickedField.copy(isRevealed = true)

                            // 2. Если она помеченная - открываем все остальные
                            if (clickedField.isMarked) {
                                fields = updatedFields.map { it.copy(isRevealed = true) }
                                gameState = DrawGameState.RESULT // Завершаем игру
                            } else {
                                fields = updatedFields
                            }
                        }
                    }
                )
            }
        }
    }
}
@Composable
private fun GridView(
    fields: List<FieldItem>,
    gameState: DrawGameState,
    onFieldClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(fields, key = { _, item -> item.id }) { index, field ->
            val isClickable = gameState == DrawGameState.GRID && !field.isRevealed
            val cardColor = if (field.isRevealed) Color.White else field.color
            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .animateContentSize()
                    .clickable(enabled = isClickable) { onFieldClick(index) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                if (field.isRevealed) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (field.isMarked) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Помеченное поле",
                                modifier = Modifier.size(48.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
// SettingsView остается без изменений
@Composable
private fun SettingsView(
    totalFields: String,
    onTotalChange: (String) -> Unit,
    markedFields: String,
    onMarkedChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text("ОБЩЕЕ КОЛИЧЕСТВО ПОЛЕЙ", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = totalFields,
            onValueChange = onTotalChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text("КОЛИЧЕСТВО ОТМЕЧЕННЫХ ПОЛЕЙ", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = markedFields,
            onValueChange = onMarkedChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DrawScreenPreview() {
    RandomizerAppTheme {
        DrawScreen(navController = rememberNavController())
    }
}