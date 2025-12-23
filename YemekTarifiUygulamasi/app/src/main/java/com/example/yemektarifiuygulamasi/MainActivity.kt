package com.example.yemektarifiuygulamasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yemektarifiuygulamasi.data.AppDatabase
import com.example.yemektarifiuygulamasi.ui.RecipeApp
import com.example.yemektarifiuygulamasi.ui.theme.YemekTarifiUygulamasiTheme // Proje adina göre değişebilir
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModel
import com.example.yemektarifiuygulamasi.viewmodel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)
        val dao = database.recipeDao()
        val viewModelFactory = RecipeViewModelFactory(dao)

        setContent {
            YemekTarifiUygulamasiTheme {
                val viewModel: RecipeViewModel = viewModel(factory = viewModelFactory)
                RecipeApp(viewModel = viewModel)
            }
        }
    }
}