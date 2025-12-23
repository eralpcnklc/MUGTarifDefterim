package com.example.yemektarifiuygulamasi.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yemektarifiuygulamasi.data.Recipe
import com.example.yemektarifiuygulamasi.ui.components.EmptyStateMessage
import com.example.yemektarifiuygulamasi.ui.components.RecipeCard
import com.example.yemektarifiuygulamasi.ui.theme.Eggshell
import com.example.yemektarifiuygulamasi.ui.theme.SageGreen
import com.example.yemektarifiuygulamasi.ui.theme.Sand
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel

@Composable
fun RecipeBookScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val recipes by viewModel.allRecipes.collectAsState()


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    var showDeleteDialog by remember { mutableStateOf(false) }
    var recipeToDelete by remember { mutableStateOf<Recipe?>(null) }

    if (showDeleteDialog && recipeToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Tarifi Sil") },
            text = { Text("${recipeToDelete?.title} tarifini silmek istediğinize emin misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    recipeToDelete?.let { viewModel.deleteRecipe(it) }
                    showDeleteDialog = false
                    recipeToDelete = null
                }) { Text("Sil", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("İptal") } }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Eggshell, Sand)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Başlık
            Column(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                Text("Bugün ne pişirelim?", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                Text(
                    text = "Tarif Defterim",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = recipes.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
                EmptyStateMessage(
                    message = "Henüz hiç tarifin yok.\nSağ alttaki + butonuyla eklemeye başla!",
                    icon = Icons.Default.MenuBook
                )
            }

            AnimatedVisibility(
                visible = recipes.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                exit = fadeOut()
            ) {

                LazyVerticalGrid(

                    columns = GridCells.Fixed(if (isLandscape) 2 else 1),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe.id) },
                            onEditClick = { onEditClick(recipe.id) },
                            onDeleteClick = {
                                recipeToDelete = recipe
                                showDeleteDialog = true
                            }
                        )
                    }
                }

            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            containerColor = SageGreen,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Ekle", modifier = Modifier.size(28.dp))
        }
    }
}