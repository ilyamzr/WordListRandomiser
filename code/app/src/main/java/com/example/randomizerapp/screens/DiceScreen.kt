package com.example.randomizerapp.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.randomizerapp.R // <<<--- ВОТ ПРАВИЛЬНЫЙ ИМПОРТ
import com.example.randomizerapp.ui.theme.RandomizerAppTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceScreen(navController: NavController) {
    var selectedDiceCount by remember { mutableStateOf(1) }
    var customDiceCount by remember { mutableStateOf("5") }
    var diceResults by remember { mutableStateOf<List<Int>>(emptyList()) }
    val rotationState = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Кубики") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val baseCount = if (selectedDiceCount == -1) {
                    customDiceCount.toIntOrNull() ?: 1
                } else {
                    selectedDiceCount
                }
                val count = baseCount.coerceIn(1, 20)
                if (selectedDiceCount == -1) customDiceCount = count.toString()

                diceResults = (1..count).map { Random.nextInt(1, 7) }
                coroutineScope.launch {
                    rotationState.snapTo(0f)
                    rotationState.animateTo(360f, animationSpec = tween(500))
                }
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Бросить кубики")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "КОЛИЧЕСТВО КУБИКОВ",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val diceCounts = listOf(1, 2, 3, 4)
                    diceCounts.forEach { count ->
                        Row(
                            Modifier.selectable(
                                selected = (selectedDiceCount == count),
                                onClick = { selectedDiceCount = count },
                                role = Role.RadioButton
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (selectedDiceCount == count), onClick = null)
                            Text(text = count.toString(), modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedDiceCount == -1),
                        onClick = { selectedDiceCount = -1 }
                    )
                    Text(text = "Другое", modifier = Modifier.padding(start = 8.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = customDiceCount,
                        onValueChange = { customDiceCount = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(100.dp),
                        singleLine = true,
                        enabled = (selectedDiceCount == -1)
                    )
                }

                if (diceResults.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Сумма: ${diceResults.sum()}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 80.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(diceResults) { result ->
                            DiceImage(
                                value = result,
                                modifier = Modifier
                                    .size(80.dp)
                                    .rotate(rotationState.value)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DiceImage(value: Int, modifier: Modifier = Modifier) {
    val imageRes = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1 // Fallback
    }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Кубик со значением $value",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DiceScreenPreview() {
    RandomizerAppTheme {
        DiceScreen(rememberNavController())
    }
}