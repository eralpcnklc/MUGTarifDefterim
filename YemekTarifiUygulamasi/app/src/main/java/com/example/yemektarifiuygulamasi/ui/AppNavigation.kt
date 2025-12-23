package com.example.yemektarifiuygulamasi.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yemektarifiuygulamasi.ui.screens.*
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel
import com.example.yemektarifiuygulamasi.ui.theme.*

sealed class Screen(val route: String) {
    data object Book : Screen("book")
    data object Pantry : Screen("pantry")
    data object Add : Screen("add")
    data object Detail : Screen("detail/{recipeId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
    data object Edit : Screen("edit/{recipeId}") {
        fun createRoute(id: Int) = "edit/$id"
    }
}

@Composable
fun RecipeApp(viewModel: RecipeViewModel) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute == Screen.Book.route || currentRoute == Screen.Pantry.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Gunmetal,
                    contentColor = Sand,
                    tonalElevation = 8.dp
                ) {

                    val navItemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        selectedTextColor = Sand,
                        indicatorColor = TerraCotta,
                        unselectedIconColor = Sand.copy(alpha = 0.6f),
                        unselectedTextColor = Sand.copy(alpha = 0.6f)
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Book, null) },
                        label = { Text("Defter") },
                        selected = currentRoute == Screen.Book.route,
                        onClick = { navController.navigate(Screen.Book.route) { popUpTo(0) } },
                        colors = navItemColors // Renkleri buraya verdik
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Kitchen, null) },
                        label = { Text("Öneri") },
                        selected = currentRoute == Screen.Pantry.route,
                        onClick = { navController.navigate(Screen.Pantry.route) { popUpTo(0) } },
                        colors = navItemColors // Renkleri buraya verdik
                    )
                }

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Book.route,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(Screen.Book.route) {
                RecipeBookScreen(
                    viewModel = viewModel,
                    onRecipeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                    onAddClick = { navController.navigate(Screen.Add.route) },
                    onEditClick = { id -> navController.navigate(Screen.Edit.createRoute(id)) }
                )
            }


            composable(Screen.Pantry.route) {
                PantryScreen(
                    viewModel = viewModel,
                    onRecipeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
                )
            }




            composable(Screen.Add.route) {
                AddRecipeScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }


            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
                RecipeDetailScreen(
                    viewModel = viewModel,
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() }
                )
            }


            composable(
                route = Screen.Edit.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
                EditRecipeScreen(
                    viewModel = viewModel,
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}