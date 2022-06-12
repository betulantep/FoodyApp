package com.betulantep.foody.data

import com.betulantep.foody.data.database.RecipesDao
import com.betulantep.foody.data.database.entities.FavoriteEntity
import com.betulantep.foody.data.database.entities.FoodJokeEntity
import com.betulantep.foody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){
    fun readRecipes(): Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJoke()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity){
        recipesDao.insertFavoriteRecipe(favoriteEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity){
        recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
}