package com.betulantep.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulantep.foody.data.database.entities.FavoriteEntity
import com.betulantep.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.betulantep.foody.util.RecipesDiffUtil

class FavoriteRecipesAdapter: RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {

    private var favoriteRecipesList = emptyList<FavoriteEntity>()

    class MyViewHolder(var binding: FavoriteRecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = FavoriteRecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.favoriteEntity = favoriteRecipesList[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = favoriteRecipesList.size

    fun setData(newFavoriteRecipes: List<FavoriteEntity>){
        val favoriteDiffUtil = RecipesDiffUtil(favoriteRecipesList,newFavoriteRecipes)
        val diffUtil = DiffUtil.calculateDiff(favoriteDiffUtil)
        favoriteRecipesList = newFavoriteRecipes
        diffUtil.dispatchUpdatesTo(this)
    }
}