package com.example.randomizerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomizerapp.ui.theme.RandomizerAppTheme // Ваша тема
import com.example.randomizerapp.screens.MainScreen
import com.example.randomizerapp.screens.NumberScreen
import com.example.randomizerapp.screens.DiceScreen
import com.example.randomizerapp.screens.CoinScreen
import com.example.randomizerapp.screens.DrawScreen
import com.example.randomizerapp.screens.lists.ListDetailScreen
import com.example.randomizerapp.screens.lists.ListsManagementScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomizerAppTheme { // Применяем нашу тему
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RandomizerAppNavigation()
                }
            }
        }
    }
}

@Composable
fun RandomizerAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController = navController)
        }
        composable("number_screen") {
            NumberScreen(navController = navController)
        }
        composable("lists_management_screen") {
            ListsManagementScreen(navController = navController)
        }
        composable(
            route = "list_detail_screen/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            ListDetailScreen(listId = listId, navController = navController)
        }
        composable("dice_screen") {
            DiceScreen(navController = navController)
        }
        composable("coin_screen") {
            CoinScreen(navController = navController)
        }
        composable("draw_screen") {
            DrawScreen(navController = navController)
        }
    }
}