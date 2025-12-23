package com.example.yemektarifiuygulamasi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .addCallback(PrepopulateCallback())
                    .fallbackToDestructiveMigration() // Şema değiştiği için eski veriyi silip yenisini kurar
                    .build()
                INSTANCE = instance
                instance
            }
        }

        class PrepopulateCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.recipeDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: RecipeDao) {
            // Varsayılan tariflerde isUserCreated = false (Zaten varsayılan değer false ama açıkça belirtelim)
            val recipes = listOf(
                Recipe(title = "Menemen", description = "Klasik Türk kahvaltısı.", ingredients = "yumurta, domates, biber, soğan, yağ", instructions = "Biberleri kavur...", difficulty = "Kolay", cookingTime = 15),
                Recipe(title = "Mercimek Çorbası", description = "Sıcak bir başlangıç.", ingredients = "kırmızı mercimek, soğan, havuç, patates", instructions = "Sebzeleri kavur...", difficulty = "Orta", cookingTime = 30),
                // ... Diğer tariflerin de sonuna isUserCreated = false ekleyebilirsin veya varsayılanı kullanır ...
                Recipe(title = "Omlet", description = "Hızlı kahvaltı.", ingredients = "yumurta, peynir, süt, tereyağı", instructions = "Yumurtaları çırp...", difficulty = "Kolay", cookingTime = 10)
            )
            dao.insertAll(recipes)
        }
    }
}