package com.betulantep.foody.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulantep.foody.R
import com.betulantep.foody.data.database.entities.FavoriteEntity
import com.betulantep.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.betulantep.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.betulantep.foody.util.RecipesDiffUtil
import com.betulantep.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipesAdapter(
    var requireActivity: FragmentActivity,
    var mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {
    private var multiselection = false
    private lateinit var rootView: View
    private lateinit var mActionMode: ActionMode
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var favoriteRecipesList = emptyList<FavoriteEntity>()

    class MyViewHolder(var binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = FavoriteRecipesRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipesList[position]
        holder.binding.favoriteEntity = currentRecipe
        holder.binding.executePendingBindings()
        /**
         * Single click listener
         */
        holder.binding.favoriteRecipeRowLayout.setOnClickListener {
            if (multiselection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.binding.favoriteRecipeRowLayout.findNavController().navigate(action)
            }
        }
        /**
         * Long click listener
         */
        holder.binding.favoriteRecipeRowLayout.setOnLongClickListener {
            if (!multiselection) {
                multiselection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiselection = false
                mActionMode.finish()
                false
            }
        }
    }

    override fun getItemCount(): Int = favoriteRecipesList.size

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.selectedStrokeColor)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favoriteRecipeRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.binding.favoritesRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "${selectedRecipes.size} item selected"
            else -> mActionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_recipes_delete_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.favoriteRecipesDeleteMenu){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed")

            multiselection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiselection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteRecipes: List<FavoriteEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipesList, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipesList = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()
    }
    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized) mActionMode.finish()
    }
}