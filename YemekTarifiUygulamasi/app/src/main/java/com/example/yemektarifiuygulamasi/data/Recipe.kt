package com.example.yemektarifiuygulamasi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: String,
    val instructions: String,
    val difficulty: String,
    val cookingTime: Int,
    val imageUri: String? = null
)