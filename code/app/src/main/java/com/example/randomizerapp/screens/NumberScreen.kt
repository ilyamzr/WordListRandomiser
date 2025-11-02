package com.example.randomizerapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.randomizerapp.ui.theme.RandomizerAppTheme
import kotlin.random.Random

val resultColors = listOf(
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF3F51B5),
    Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFFFF9800)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberScreen(navController: NavController) {
    var minNumber by remember { mutableStateOf(0) }
    var maxNumber by remember { mutableStateOf(100) }
    var quantity by remember { mutableStateOf(1) }

    var showMinDialog by remember { mutableStateOf(false) }
    var showMaxDialog by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }

    var result by remember { mutableStateOf<String?>(null) }
    var resultColor by remember { mutableStateOf(resultColors.first()) }

    // Диалоги для редактирования
    if (showMinDialog) {
        EditNumberDialog(
            title = "Минимальное число",
            initialValue = minNumber,
            onDismiss = { showMinDialog = false },
            onConfirm = {
                minNumber = it
                showMinDialog = false
            }
        )
    }
    if (showMaxDialog) {
        EditNumberDialog(
            title = "Максимальное число",
            initialValue = maxNumber,
            onDismiss = { showMaxDialog = false },
            onConfirm = {
                maxNumber = it
                showMaxDialog = false
            }
        )
    }
    if (showQuantityDialog) {
        EditNumberDialog(
            title = "Количество чисел",
            initialValue = quantity,
            onDismiss = { showQuantityDialog = false },
            onConfirm = {
                quantity = it
                showQuantityDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Число") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (minNumber <= maxNumber) {
                    result = (1..quantity).joinToString(", ") {
                        Random.nextInt(minNumber, maxNumber + 1).toString()
                    }
                    resultColor = resultColors.random()
                } else {
                    result = "Ошибка"
                    resultColor = Color.Red
                }
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Генерировать")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "ОТ", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "$minNumber",
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable { showMinDialog = true }
                )
                Text(text = "ДО", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "$maxNumber",
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable { showMaxDialog = true }
                )
                Spacer(modifier = Modifier.height(48.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    onClick = { showQuantityDialog = true }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Количество")
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = result != null,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ResultTile(
                    text = result ?: "",
                    color = resultColor,
                    onClick = { result = null }
                )
            }
        }
    }
}

@Composable
fun EditNumberDialog(
    title: String,
    initialValue: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var textValue by remember { mutableStateOf(initialValue.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it.filter { char -> char.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(textValue.toIntOrNull() ?: 0) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun ResultTile(text: String, color: Color, onClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.6f),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .sizeIn(
                        minWidth = 200.dp,
                        minHeight = 200.dp,
                        maxWidth = 320.dp,
                        maxHeight = 450.dp
                    ),
                // МОДИФИКАТОР .wrapContentHeight(unbounded = true) БЫЛ УДАЛЕН ОТСЮДА
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 65.sp,
                        modifier = Modifier.verticalScroll(scrollState)
                    )
                }
            }
        }
    }
}