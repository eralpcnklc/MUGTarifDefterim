package com.example.yemektarifiuygulamasi.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yemektarifiuygulamasi.ui.components.EmptyStateMessage
import com.example.yemektarifiuygulamasi.ui.components.ModernSearchBar
import com.example.yemektarifiuygulamasi.ui.components.RecipeCard
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel

@Composable
fun PantryScreen(viewModel: RecipeViewModel, onRecipeClick: (Int) -> Unit) {
    val ingredients by viewModel.pantryIngredients.collectAsState()
    val recommendations by viewModel.recommendedRecipes.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Ne Pişirsem?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Elinizdeki malzemelere göre tarif önerelim.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(20.dp))


        ModernSearchBar(
            value = ingredients,
            onValueChange = { viewModel.updatePantry(it) },
            placeholder = "Örn: Domates, Yumurta, Soğan"
        )

        Spacer(modifier = Modifier.height(24.dp))


        AnimatedVisibility(visible = recommendations.isNotEmpty()) {
            Text(
                "${recommendations.size} Tarif Önerildi",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }


        Column(modifier = Modifier.fillMaxSize()) {
            if (ingredients.isNotEmpty() && recommendations.isEmpty()) {

                EmptyStateMessage(
                    message = "Bu malzemelerle eşleşen bir tarif bulamadık. Başka malzemeler dene!",
                    icon = Icons.Default.SentimentDissatisfied
                )
            } else if (ingredients.isEmpty()) {

                EmptyStateMessage(
                    message = "Malzemeleri yukarıya yazmaya başla",
                    icon = Icons.Default.Search
                )
            } else {

                AnimatedVisibility(
                    visible = recommendations.isNotEmpty(),
                    enter = slideInVertically(initialOffsetY = { it / 4 }) + fadeIn()
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(recommendations) { recipe ->
                            RecipeCard(
                                recipe = recipe, 
                                onClick = { onRecipeClick(recipe.id) },
                                onEditClick = {},
                                onDeleteClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}