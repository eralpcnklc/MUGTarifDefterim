package com.example.yemektarifiuygulamasi.ui.screens

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yemektarifiuygulamasi.ui.components.getDifficultyColor
import com.example.yemektarifiuygulamasi.ui.theme.SageGreen
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(viewModel: RecipeViewModel, recipeId: Int, onBack: () -> Unit) {
    val recipeState = viewModel.getRecipeById(recipeId).collectAsState(initial = null)
    val recipe = recipeState.value


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(recipe?.title ?: "Yükleniyor...", maxLines = 1) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        if (recipe != null) {
            if (isLandscape) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // SOL: Resim
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        if (recipe.imageUri != null) {
                            AsyncImage(
                                model = Uri.parse(recipe.imageUri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {}
                        }
                    }


                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetailContent(recipe)
                    }
                }
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (recipe.imageUri != null) {
                        AsyncImage(
                            model = Uri.parse(recipe.imageUri),
                            contentDescription = recipe.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        DetailContent(recipe)
                    }
                }
            }
        }
    }
}


@Composable
fun DetailContent(recipe: com.example.yemektarifiuygulamasi.data.Recipe) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, null, tint = SageGreen)
                Text(" ${recipe.cookingTime} dk", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            VerticalDivider(modifier = Modifier.height(20.dp))
            Text(
                text = recipe.difficulty,
                style = MaterialTheme.typography.titleMedium,
                color = getDifficultyColor(recipe.difficulty),
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(text = recipe.description, style = MaterialTheme.typography.titleMedium, color = Color.DarkGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

    Text("Malzemeler", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(12.dp))
    recipe.ingredients.split(",").forEach { item ->
        if (item.isNotBlank()) {
            Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, null, modifier = Modifier.size(24.dp), tint = SageGreen)
                Text(text = " ${item.trim()}", modifier = Modifier.padding(start = 12.dp), style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

    Text("Hazırlanışı", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(12.dp))
    Text(text = recipe.instructions, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp), lineHeight = 28.sp)
    Spacer(modifier = Modifier.height(50.dp))
}