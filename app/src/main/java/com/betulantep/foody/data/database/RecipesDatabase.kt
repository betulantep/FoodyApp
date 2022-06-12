package com.betulantep.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.betulantep.foody.data.database.entities.FavoriteEntity
import com.betulantep.foody.data.database.entities.FoodJokeEntity
import com.betulantep.foody.data.database.entities.RecipesEntity

@Database(entities = [RecipesEntity::class, FavoriteEntity::class, FoodJokeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}