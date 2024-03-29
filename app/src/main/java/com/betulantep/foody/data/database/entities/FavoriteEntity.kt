package com.betulantep.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.betulantep.foody.models.Result
import com.betulantep.foody.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)