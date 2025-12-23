package com.example.yemektarifiuygulamasi.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yemektarifiuygulamasi.data.Recipe
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel
import com.example.yemektarifiuygulamasi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(viewModel: RecipeViewModel, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val difficultyOptions = listOf("Kolay", "Orta", "Zor")
    var selectedDifficulty by remember { mutableStateOf("Kolay") }
    var cookingTime by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Yeni Tarif Ekle", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Cursive, color = MaterialTheme.colorScheme.primary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Seçilen resim",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Fotoğraf Ekle (İsteğe Bağlı)", color = Color.Gray)
                    }
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Yemek Adı") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Kısa Açıklama") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Malzemeler (Virgülle ayırın)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Nasıl Yapılır?") }, modifier = Modifier.fillMaxWidth(), minLines = 5)

            Text("Zorluk Seviyesi:", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                difficultyOptions.forEach { option ->
                    FilterChip(
                        selected = selectedDifficulty == option,
                        onClick = { selectedDifficulty = option },
                        label = { Text(option) },
                        leadingIcon = if (selectedDifficulty == option) { { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) } } else null
                    )
                }
            }

            OutlinedTextField(
                value = cookingTime,
                onValueChange = { if (it.all { char -> char.isDigit() }) cookingTime = it },
                label = { Text("Pişirme Süresi (dk)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (title.isNotEmpty() && ingredients.isNotEmpty()) {
                        viewModel.addRecipe(
                            Recipe(
                                title = title,
                                description = description,
                                ingredients = ingredients,
                                instructions = instructions,
                                difficulty = selectedDifficulty,
                                cookingTime = cookingTime.toIntOrNull() ?: 0,
                                imageUri = selectedImageUri?.toString()
                            )
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerraCotta,
                    contentColor = White
                )

            ) {
                Text("Tarifi Kaydet", fontSize = 16.sp)
            }
        }
    }
}