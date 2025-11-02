package com.example.randomizerapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.randomizerapp.ui.theme.RandomizerAppTheme
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinScreen(navController: NavController) {
    var coinResult by remember { mutableStateOf("?") }
    var isFlipping by remember { mutableStateOf(false) }

    val rotationY by animateFloatAsState(
        targetValue = if (isFlipping) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "coinFlipAnimation",
        finishedListener = {
            if (isFlipping) {
                coinResult = if (Random.nextBoolean()) "Орёл" else "Решка"
                isFlipping = false // Завершаем переворот
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Монетка") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Доп. меню */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Еще")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (!isFlipping) {
                    isFlipping = true
                }
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Подбросить монетку")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Coin(
                    rotationY = rotationY,
                    isFlipping = isFlipping
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = if (isFlipping) "..." else coinResult,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun Coin(rotationY: Float, isFlipping: Boolean) {
    val coinFace = if (!isFlipping && rotationY < 90f) "О" else "Р"

    Card(
        modifier = Modifier
            .size(150.dp)
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 8 * density
            },
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700)) // Золотой цвет
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = coinFace,
                fontSize = 80.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinScreenPreview() {
    RandomizerAppTheme {
        CoinScreen(rememberNavController())
    }
}