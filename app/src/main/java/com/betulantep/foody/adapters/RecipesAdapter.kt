package com.betulantep.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulantep.foody.databinding.RecipesRowLayoutBinding
import com.betulantep.foody.models.FoodRecipe
import com.betulantep.foody.models.Result
import com.betulantep.foody.util.RecipesDiffUtil

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
    private var recipes = emptyList<Result>()
    class MyViewHolder(private val binding: RecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}