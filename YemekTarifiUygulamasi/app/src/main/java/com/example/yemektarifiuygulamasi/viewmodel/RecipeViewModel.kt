package com.example.yemektarifiuygulamasi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yemektarifiuygulamasi.data.Recipe
import com.example.yemektarifiuygulamasi.data.RecipeDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

class RecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    val allRecipes: StateFlow<List<Recipe>> = recipeDao.getAllRecipes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _pantryIngredients = MutableStateFlow("")
    val pantryIngredients = _pantryIngredients.asStateFlow()

    val recommendedRecipes: StateFlow<List<Recipe>> = combine(allRecipes, _pantryIngredients) { recipes, input ->
        if (input.isBlank()) {
            emptyList()
        } else {
            val inputs = input.lowercase(Locale.getDefault()).split(",").map { it.trim() }.filter { it.isNotEmpty() }
            recipes.filter { recipe ->
                val recipeIngredients = recipe.ingredients.lowercase(Locale.getDefault())
                inputs.any { userIngredient -> recipeIngredients.contains(userIngredient) }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updatePantry(input: String) {
        _pantryIngredients.value = input
    }

    fun getRecipeById(id: Int): Flow<Recipe> = recipeDao.getRecipeById(id)

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeDao.insert(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch { recipeDao.delete(recipe) }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch { recipeDao.update(recipe) }
    }
}

class RecipeViewModelFactory(private val dao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}