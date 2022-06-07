package com.betulantep.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.betulantep.foody.R
import com.betulantep.foody.databinding.IngredientsRowLayoutBinding
import com.betulantep.foody.models.ExtendedIngredient
import com.betulantep.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.betulantep.foody.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {
    private var ingredientsList = emptyList<ExtendedIngredient>()
    class MyViewHolder(var binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(extendedIngredient: ExtendedIngredient){
                binding.extendedIngredient = extendedIngredient
                binding.executePendingBindings()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ivIngredientImage.load(BASE_IMAGE_URL + ingredientsList[position].image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.ingredientName.text = ingredientsList[position].name.replaceFirstChar { it.uppercase() }
        val currentIngredient = ingredientsList[position]
        holder.bind(currentIngredient)
    }

    override fun getItemCount(): Int = ingredientsList.size

    fun setData(newIngredients: List<ExtendedIngredient>){
        val ingredientDiffUtil = RecipesDiffUtil(ingredientsList,newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}